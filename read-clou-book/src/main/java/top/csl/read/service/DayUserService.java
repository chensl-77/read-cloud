package top.csl.read.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.pojo.book.DayBook;
import top.csl.read.common.pojo.book.DayUser;
import top.csl.read.common.result.Result;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 11:45
 **/
public interface DayUserService extends IService<DayUser> {
    Result<DayUser> savedayuser(Integer userId);
}
