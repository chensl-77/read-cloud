package top.csl.read.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.pojo.book.DayBook;
import top.csl.read.common.result.Result;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 11:42
 **/
public interface DayBookService extends IService<DayBook> {
    /**
     * 发布每日一书
     * @param bookId
     * @return
     */
    Result<Book> saveBook(String bookId);

    /**
     * 获取当日一书
     * @return
     */
    String getNowBookId();
}
