package top.csl.read.controller;

import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.pojo.book.DayBook;
import top.csl.read.common.pojo.book.DayUser;
import top.csl.read.common.result.Result;
import top.csl.read.service.DayUserService;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 11:44
 **/
@RestController
@RequestMapping("book")
public class DayUserController {

    @Autowired
    private DayUserService dayUserService;

    @ApiOperation(value = "添加每日获得图书用户" , httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "int"),
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Book.class)})
    @PostMapping("/savedayuser")
    public Result<DayUser> savedayuser(@RequestParam Integer userId){
        return dayUserService.savedayuser(userId);
    }
}
