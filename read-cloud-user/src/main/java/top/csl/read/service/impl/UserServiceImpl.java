package top.csl.read.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import top.csl.read.client.BookService;
import top.csl.read.common.constant.RabbitMQConstant;
import top.csl.read.common.pojo.account.User;
import top.csl.read.common.result.Result;
import top.csl.read.common.result.ResultUtil;
import top.csl.read.common.utils.DateUtil;
import top.csl.read.common.utils.RedisUtil;
import top.csl.read.mapper.UserMapper;
import top.csl.read.param.UserParam;
import top.csl.read.service.UserService;
import top.csl.read.utils.JWTUtil;
import top.csl.read.utils.UserUtil;
import top.csl.read.vo.AuthVO;
import top.csl.read.vo.UserVO;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static top.csl.read.common.constant.JwtConstant.EXPIRE_DAY;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 12:42
 **/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DateUtil dateUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public Result register(UserParam userParam) {
        User user = userMapper.selectByLoginName(userParam.getLoginName());
        if (user != null) {
            return ResultUtil.verificationFailed().buildMessage(userParam.getLoginName() + "???????????????????????????????????????????????????");
        }
        user = new User();
        BeanUtils.copyProperties(userParam, user);
        String encryptPwd = UserUtil.getUserEncryptPassword(userParam.getLoginName(), userParam.getUserPwd());
        user.setUserPwd(encryptPwd);
        try {
            // ??????????????????
            user.setHeadImgUrl("http://chensl.top/img/webavatar.10e5eb5a.jpg");
            user.setUuid(UUID.randomUUID().toString().replace("-", ""));
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            this.userMapper.insert(user);
        } catch (Exception ex) {
            log.error("????????????????????????{}; user:{}", ex, user);
            return ResultUtil.fail().buildMessage("?????????????????????????????????! ???(#>??<)???");
        }
        return ResultUtil.success().buildMessage("???????????????????????????");
    }

    @Override
    public Result<AuthVO> login(String loginName, String password) {
        try {
            User user = this.userMapper.selectByLoginName(loginName);
            if (null == user) {
                return ResultUtil.notFound().buildMessage("?????????????????????????????????");
            }

            // ??????????????????
            String encryptPwd = UserUtil.getUserEncryptPassword(loginName, password);
            if (!user.getUserPwd().equals(encryptPwd)) {
                return ResultUtil.verificationFailed().buildMessage("????????????????????????????????????");
            }

            // ?????????????????????????????????
            AuthVO vo = new AuthVO();
            UserVO userVo = new UserVO();
            BeanUtils.copyProperties(user, userVo);
            String token = JWTUtil.buildJwt(this.getLoginExpre(), userVo);
            redisTemplate.opsForValue().set(token, JSON.toJSONString(userVo), 7, TimeUnit.DAYS);
            vo.setToken(token);
            vo.setUser(userVo);
            return ResultUtil.success(vo);
        } catch (Exception ex) {
            log.error("??????????????????{}; loginName:{}", ex, loginName);
            return ResultUtil.fail().buildMessage("????????????????????????????????????(#>??<)??? ??????????????? ");
        }
    }

    @Override
    public Result logout(String loginName) {
        return null;
    }

    @Override
    public Result update(UserParam userParam) {
        return null;
    }

    @Override
    public Result updatePassword(UserParam userParam) {
        return null;
    }

    @Override
    public Result sign(Integer userId) {
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        String dateStr = dateUtil.dateFormatB();
        String signkey = "sign:" + userId + ":" + dateStr;
        redisTemplate.opsForValue().setBit(signkey, dayOfMonth - 1, true);
        taskSign(userId);
        return ResultUtil.success();
    }

    @Override
    public Result signdays(Integer userId) {
        String dateStr = dateUtil.dateFormatB();
        String key = "sign:" + userId + ":" + dateStr;
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        List<Long> res = redisTemplate.opsForValue().bitField(key,
                BitFieldSubCommands.create().
                        get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0));
        if (res == null || res.isEmpty()) {
            return ResultUtil.success(0);
        }
        Long result = res.get(0);
        if (result == null || result == 0) {
            return ResultUtil.success(0);
        }
        int signdays = 0;
        //???????????????
//        char[] chars = Integer.toBinaryString(result.intValue()).toCharArray();
//        int i = chars.length;
//        while (i > 0){
//            if (chars[i-1] == '1') {
//                signdays++;
//            }else {
//                break;
//            }
//            i--;
//        }
        //??????????????? result & 1??????????????????bit???
        while (true) {
            if ((result & 1) == 1) {
                signdays++;
            } else {
                break;
            }
            result >>>= 1;
        }
        return ResultUtil.success(signdays);
    }

    @Override
    public Result daybook(Integer userId, String bookId) {
        try {
            String dateStr = dateUtil.dateFormatA();
            String key = "dayBook" + ":" + dateStr;
            boolean success = redisUtil.isExistKey(key);
            if (success) {
                return ResultUtil.custom(201, "????????????????????????????????????");
            }
            return bookService.publishdaybook(bookId);
        }catch (Exception e){
            log.error("??????????????????{}; bookId:{}", e, bookId);
            return ResultUtil.fail().buildMessage("????????????????????????????????????(#>??<)??? ??????????????? ");
        }
    }


    /**
     * ??????????????????
     * @param userId
     */
    public void taskSign(Integer userId){
        String dayStr = dateUtil.dateFormatA();
//        executorService.submit(() -> {
            // 1.??????lua??????
            Long result = redisTemplate.execute(
                    SECKILL_SCRIPT,
                    Collections.emptyList(),
                    dayStr, userId.toString()
            );
            int r = result.intValue();
            if (r == 0){
                Map<String, Object> map = getMap(userId);
                rabbitTemplate.convertAndSend(RabbitMQConstant.TOPIC_EXCHANGE_NAME,"sign.csl", map);
            }
//        });
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    private Date getLoginExpre() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, EXPIRE_DAY);
        return calendar.getTime();
    }

    /**
     * ??????RabbitMQ????????????
     * @param msg
     * @return
     */
    private Map<String, Object> getMap(Integer msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", msg);
        return map;
    }


}
