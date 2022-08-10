package top.csl.read.controller;

import org.springframework.web.bind.annotation.GetMapping;
import top.csl.read.common.result.Result;
import top.csl.read.common.result.ResultUtil;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 17:01
 **/
public class FallbackController {

    @GetMapping("/fallback")
    public Result fallback() {
        return ResultUtil.fail();
    }
}
