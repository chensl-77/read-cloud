package top.csl.read.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.csl.read.common.pojo.book.DayBook;
import top.csl.read.common.pojo.book.DayUser;
import top.csl.read.common.result.Result;
import top.csl.read.common.result.ResultUtil;
import top.csl.read.common.utils.DateUtil;
import top.csl.read.mapper.DayUserMapper;
import top.csl.read.service.DayBookService;
import top.csl.read.service.DayUserService;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 11:45
 **/
@Service
@Slf4j
public class DayUserServiceImpl extends ServiceImpl<DayUserMapper, DayUser> implements DayUserService {

    @Autowired
    private DayUserMapper dayUserMapper;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private DayBookService dayBookService;

    @Override
    @Transactional
    public Result<DayUser> savedayuser(Integer userId) {
        String bookId = dayBookService.getNowBookId();
        String dayStr = dateUtil.dateFormatA();
        LambdaQueryWrapper<DayUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DayUser::getCreateDay, dayStr);
        lqw.eq(DayUser::getBookId, bookId);
        lqw.eq(DayUser::getUserId, userId);
        DayUser dayUser = dayUserMapper.selectOne(lqw);
        //验证是否重复领取
        if (dayUser != null) {
            log.error("用户已经领取过一次！");
            return ResultUtil.custom(201, "今天已经领取了");
        }
        // 扣减优惠库存
        boolean success = dayBookService.update()
                .setSql("stock = stock - 1") // set stock = stock - 1
                .eq("book_id", bookId).eq("create_day",dayStr).gt("stock", 0) // where id = ? and stock > 0
                .update();
        if (!success){
            // 扣减失败
            log.error("库存不足！");
            return ResultUtil.custom(202,"今天的优惠名额已经没有了，明天再试吧！");
        }
        //创建dayUser
        dayUser = new DayUser();
        dayUser.setBookId(bookId);
        dayUser.setCreateDay(dayStr);
        dayUser.setUserId(userId);
        save(dayUser);
        return ResultUtil.success(dayUser);
    }
}
