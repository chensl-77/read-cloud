package top.csl.read.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.result.Result;
import top.csl.read.service.DayBookService;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 11:41
 **/
@Api(description = "每日图书接口")
@RestController
@RequestMapping("book")
public class DayBookController {

    @Autowired
    private DayBookService dayBookService;

    @ApiOperation(value = "发布每日图书" , httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Book.class)})
    @GetMapping("/publishdaybook")
    public Result getBookById(@RequestParam String bookId){
        return dayBookService.saveBook(bookId);
    }

}
