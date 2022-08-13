package top.csl.read.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import top.csl.read.common.result.Result;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 19:03
 **/
@Component
@FeignClient(value = "read-cloud-book")
public interface BookService {

    @RequestMapping("/book/savedayuser")
    Result savedayuser(@RequestBody Integer userId);

}
