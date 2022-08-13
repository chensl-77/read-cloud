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
import top.csl.read.config.RabbitMQConfig;
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
            return ResultUtil.verificationFailed().buildMessage(userParam.getLoginName() + "已存在，请使用其它登录名进行注册！");
        }
        user = new User();
        BeanUtils.copyProperties(userParam, user);
        String encryptPwd = UserUtil.getUserEncryptPassword(userParam.getLoginName(), userParam.getUserPwd());
        user.setUserPwd(encryptPwd);
        try {
            // 设置默认头像
            user.setHeadImgUrl("http://chensl.top/img/webavatar.10e5eb5a.jpg");
            user.setUuid(UUID.randomUUID().toString().replace("-", ""));
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            this.userMapper.insert(user);
        } catch (Exception ex) {
            log.error("注册用户失败了！{}; user:{}", ex, user);
            return ResultUtil.fail().buildMessage("注册失败，服务器被吃了! ＝(#>д<)ﾉ");
        }
        return ResultUtil.success().buildMessage("注册成功！请登录吧");
    }

    @Override
    public Result<AuthVO> login(String loginName, String password) {
        try {
            User user = this.userMapper.selectByLoginName(loginName);
            if (null == user) {
                return ResultUtil.notFound().buildMessage("登录失败，用户不存在！");
            }

            // 校验用户密码
            String encryptPwd = UserUtil.getUserEncryptPassword(loginName, password);
            if (!user.getUserPwd().equals(encryptPwd)) {
                return ResultUtil.verificationFailed().buildMessage("登录失败，密码输入错误！");
            }

            // 登录成功，返回用户信息
            AuthVO vo = new AuthVO();
            UserVO userVo = new UserVO();
            BeanUtils.copyProperties(user, userVo);
            String token = JWTUtil.buildJwt(this.getLoginExpre(), userVo);
            redisTemplate.opsForValue().set(token, JSON.toJSONString(userVo), 7, TimeUnit.DAYS);
            vo.setToken(token);
            vo.setUser(userVo);
            return ResultUtil.success(vo);
        } catch (Exception ex) {
            log.error("登录失败了！{}; loginName:{}", ex, loginName);
            return ResultUtil.fail().buildMessage("登录失败，服务器被吃了＝(#>д<)ﾉ ！请重试。 ");
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
        //第一种方法
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
        //第二种方法 result & 1得到最后一个bit位
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
            String key = "dayBook" + ":" + dateStr + ":";
            boolean success = redisUtil.isExistKey(key);
            if (success) {
                return ResultUtil.custom(201, "今天的每日一书已经发布了");
            }
            return bookService.publishdaybook(bookId);
        }catch (Exception e){
            log.error("发布失败了！{}; bookId:{}", e, bookId);
            return ResultUtil.fail().buildMessage("发布失败，服务器被吃了＝(#>д<)ﾉ ！请重试。 ");
        }
    }


    /**
     * 领取每日福利
     * @param userId
     */
    public void taskSign(Integer userId){
        String dayStr = dateUtil.dateFormatA();
        executorService.submit(() -> {
            // 1.执行lua脚本
            Long result = redisTemplate.execute(
                    SECKILL_SCRIPT,
                    Collections.emptyList(),
                    dayStr, userId.toString()
            );
            int r = result.intValue();
            if (r == 0){
                Map<String, Object> map = getMap(userId);
                rabbitTemplate.convertAndSend(RabbitMQConstant.TOPIC_EXCHANGE_NAME,"sign", map);
            }
        },"taskSign");
    }

    /**
     * 获取登陆过期时间
     *
     * @return
     */
    private Date getLoginExpre() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, EXPIRE_DAY);
        return calendar.getTime();
    }

    /**
     * 创建RabbitMQ消息对象
     * @param msg
     * @return
     */
    private Map<String, Object> getMap(Integer msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", msg);
        return map;
    }


}
