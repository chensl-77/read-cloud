package top.csl.read.common.pojo.book;

import lombok.Data;

/**
 * 每日优惠一书
 * @Author: csl
 * @DateTime: 2022/8/12 11:28
 **/
@Data
public class DayBook {

    /** 主键ID */
    protected Integer id;

    /** 图书id */
    private String bookId;

    /** 图书库存 */
    private int stock;

    /** 发布时间 */
    private String createDay;
}
