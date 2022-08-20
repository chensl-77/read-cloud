package top.csl.read.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import top.csl.read.common.pojo.book.DayBook;
import top.csl.read.common.pojo.book.DayUser;
import top.csl.read.common.result.Result;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 19:03
 **/
@Component
@FeignClient(value = "read-cloud-book")
public interface BookService {

    @PostMapping("/book/savedayuser")
    Result<DayUser> savedayuser(@RequestParam("userId") Integer userId);

}
