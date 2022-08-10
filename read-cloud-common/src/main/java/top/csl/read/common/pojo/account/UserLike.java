package top.csl.read.common.pojo.account;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户喜欢
 * @Author: csl
 * @DateTime: 2022/8/10 8:48
 **/
@Data
public class UserLike implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 用户
     */
    private Integer userId;

    /**
     * 图书id
     */
    private String bookId;

    /**
     * 创建时间
     */
    private Date createTime;

}
