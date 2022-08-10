package top.csl.read.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.csl.read.common.pojo.account.User;
import top.csl.read.common.result.Result;
import top.csl.read.param.UserParam;
import top.csl.read.vo.AuthVO;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 12:35
 **/
public interface UserService extends IService<User> {

    /**
     * 注册账户
     * @param userParam
     * @return
     */
    Result register(UserParam userParam);

    /**
     * 登录
     * @param loginName
     * @param password
     * @return
     */
    Result<AuthVO> login(String loginName, String password);

    /**
     * 退出登录
     * @param loginName
     * @return
     */
    Result logout(String loginName);

    /**
     * 修改账户
     * @param userParam
     * @return
     */
    Result update(UserParam userParam);

    /**
     * 修改密码
     * @param userParam
     * @return
     */
    Result updatePassword(UserParam userParam);

    /**
     * 签到
     * @param userId
     * @return
     */
    Result sign(Integer userId);

    /**
     * 统计连续签到天数
     * @param userId
     * @return
     */
    Result signdays(Integer userId);
}

