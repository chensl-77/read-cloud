package top.csl.read.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.result.Result;
import top.csl.read.vo.BookVO;

/**
 * @Author: csl
 * @DateTime: 2022/8/11 10:25
 **/
public interface BookService extends IService<Book> {
    Result<Book> getBookById(String bookId);

    Result<BookVO> getBookDetails(String bookId);
}
