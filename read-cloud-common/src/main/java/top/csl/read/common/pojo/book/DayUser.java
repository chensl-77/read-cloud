package top.csl.read.common.pojo.book;

import lombok.Data;


/**
 * 每日签到获得优惠list
 * @Author: csl
 * @DateTime: 2022/8/12 11:36
 **/
@Data
public class DayUser {

    /** 主键ID */
    protected Integer id;

    /** 图书id */
    private String bookId;

    /** 用户id */
    private Integer userId;

    /** 签到时间/领取事件 */
    protected String createDay;
}
