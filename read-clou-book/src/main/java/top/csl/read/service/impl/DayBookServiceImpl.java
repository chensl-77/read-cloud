package top.csl.read.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.pojo.book.DayBook;
import top.csl.read.common.result.Result;
import top.csl.read.common.result.ResultUtil;
import top.csl.read.common.utils.DateUtil;
import top.csl.read.common.utils.RedisUtil;
import top.csl.read.mapper.DayBookMapper;
import top.csl.read.service.DayBookService;

import static top.csl.read.common.constant.JwtConstant.STOCK_COUNTS;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 11:42
 **/
@Service
public class DayBookServiceImpl extends ServiceImpl<DayBookMapper,DayBook> implements DayBookService {

    @Autowired
    private DayBookMapper dayBookMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DateUtil dateUtil;

    @Override
    @Transactional
    public Result<Book> saveBook(String bookId) {
        String dateStr = dateUtil.dateFormatA();
        String key = "dayBook:stock" + dateStr;
        redisUtil.setKey(key,"1",redisUtil.getSecondsNextEarlyMorning());
        redisUtil.incr(key,STOCK_COUNTS);
        DayBook dayBook = new DayBook();
        dayBook.setBookId(bookId);
        dayBook.setStock(10);
        dayBook.setCreateDay(dateStr);
        dayBookMapper.insert(dayBook);
        return ResultUtil.success("发布成功");
    }

    @Override
    public String getNowBookId() {
        String dateStr = dateUtil.dateFormatA();
        LambdaQueryWrapper<DayBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DayBook::getCreateDay,dateStr);
        DayBook dayBook = dayBookMapper.selectOne(lqw);
        return dayBook.getBookId();
    }
}
