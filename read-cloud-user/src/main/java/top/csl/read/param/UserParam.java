package top.csl.read.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 12:27
 **/
@ApiModel(value = "用户注册信息")
@Data
public class UserParam {
    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名(至少4个字符)", example = "csl")
    private String loginName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", example = "123456")
    private String userPwd;

    /**
     * 中文名
     */
    @ApiModelProperty(value = "昵称", example = "海绵宝宝")
    private String nickName;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "手机", example = "12345678910")
    private String phoneNumber;
}
