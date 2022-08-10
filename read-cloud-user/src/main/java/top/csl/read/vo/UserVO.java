package top.csl.read.vo;

import lombok.Data;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 13:12
 **/
@Data
public class UserVO {
    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 唯一id
     */
    private String uuid;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 中文名
     */
    private String nickName;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 头像
     */
    private String headImgUrl;
}
