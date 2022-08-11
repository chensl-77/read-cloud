package top.csl.read.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.result.Result;
import top.csl.read.common.result.ResultUtil;
import top.csl.read.mapper.BookMapper;
import top.csl.read.service.BookService;
import top.csl.read.vo.BookVO;

/**
 * @Author: csl
 * @DateTime: 2022/8/11 10:27
 **/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Result<Book> getBookById(String bookId) {
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Book::getBookId,bookId);
        Book book = bookMapper.selectOne(lqw);
        return ResultUtil.success(book);
    }

    @Override
    public Result<BookVO> getBookDetails(String bookId) {
        return null;
    }
}
