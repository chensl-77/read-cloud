package top.csl.read.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.result.Result;
import top.csl.read.service.BookService;
import top.csl.read.vo.BookVO;

/**
 * @Author: csl
 * @DateTime: 2022/8/11 10:10
 **/
@Api(description = "图书查询接口")
@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @ApiOperation(value = "查询图书基本信息" , httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Book.class)})
    @GetMapping("/getBookById")
    public Result<Book> getBookById(String bookId){
        return bookService.getBookById(bookId);
    }

    @ApiOperation(value = "获取图书详情" , httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Book.class)})
    @GetMapping("/details")
    public Result<BookVO> getBookDetails(String bookId){
        return bookService.getBookDetails(bookId);
    }
}
