package top.csl.read.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.result.Result;

/**
 * @Author: csl
 * @DateTime: 2022/8/11 10:55
 **/
@Component
@FeignClient(value = "read-cloud-book")
public interface BookService {

    @RequestMapping("/book/getBookById")
    Result<Book> getBookById(@RequestParam("bookId") String bookId);

    @RequestMapping("/book/publishdaybook")
    Result<Book> publishdaybook(@RequestParam("bookId") String bookId);
}
