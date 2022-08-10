package top.csl.read.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.csl.read.common.pojo.account.User;
import top.csl.read.common.result.Result;
import top.csl.read.common.result.ResultUtil;
import top.csl.read.mapper.UserMapper;
import top.csl.read.param.UserParam;
import top.csl.read.service.UserService;
import top.csl.read.utils.UserUtil;
import top.csl.read.vo.AuthVO;

import java.util.UUID;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 12:42
 **/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result register(UserParam userParam) {
        User user = userMapper.selectById(userParam.getLoginName());
        if (user != null) {
            return ResultUtil.verificationFailed().buildMessage(userParam.getLoginName() + "已存在，请使用其它登录名进行注册！");
        }
        user = new User();
        BeanUtils.copyProperties(userParam, user);
        String encryptPwd = UserUtil.getUserEncryptPassword(userParam.getLoginName(), userParam.getUserPwd());
        user.setUserPwd(encryptPwd);
        try{
            // 设置默认头像
            user.setHeadImgUrl("http://chensl.top/img/webavatar.10e5eb5a.jpg");
            user.setUuid(UUID.randomUUID().toString().replace("-",""));
            this.userMapper.insert(user);
        } catch (Exception ex) {
            log.error("注册用户失败了！{}; user:{}", ex, user);
            return ResultUtil.fail().buildMessage("注册失败，服务器被吃了! ＝(#>д<)ﾉ");
        }
        return ResultUtil.success().buildMessage("注册成功！请登录吧");
    }

    @Override
    public Result<AuthVO> login(String loginName, String password) {
        return null;
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
        return null;
    }

    @Override
    public Result signdays(Integer userId) {
        return null;
    }
}
