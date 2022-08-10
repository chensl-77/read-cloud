package top.csl.read.common.pojo.book;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 8:58
 **/
@Data
public class BookAuthor implements Serializable {

    private static final Long serialVersionUID = 1L;

    /** 主键ID */
    protected Integer id;

    /**
     * 作者名称
     */
    private String name;

    /**
     * 作者简介
     */
    private String introduction;

    /**
     * 头像附件id
     */
    private String headImgUrl;

    /** 创建人 */
    protected String creater;
    /** 创建时间 */
    protected Date createTime;
    /** 修改人 */
    protected String updater;
    /** 修改时间 */
    protected Date updateTime;

}
