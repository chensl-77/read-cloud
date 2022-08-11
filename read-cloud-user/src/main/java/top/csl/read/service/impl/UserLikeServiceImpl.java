package top.csl.read.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.csl.read.client.BookService;
import top.csl.read.common.pojo.account.UserLike;
import top.csl.read.common.pojo.book.Book;
import top.csl.read.common.result.Result;
import top.csl.read.common.result.ResultUtil;
import top.csl.read.common.vo.SimpleBookVO;
import top.csl.read.mapper.UserLikeMapper;
import top.csl.read.service.UserLikeService;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: csl
 * @DateTime: 2022/8/11 10:33
 **/
@Service
@Slf4j
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike> implements UserLikeService {

    @Autowired
    @Lazy
    private BookService bookService;

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Override
    public Result likeSeeClick(Integer userId, String bookId, Integer value) {
        return null;
    }

    @Override
    public Result<Integer> getBookLikesCount(String bookId) {
        return null;
    }

    @Override
    public Result getUserLikeBookList(Integer userId, Integer page, Integer size) {
        try {
            String orderBy = "create_time desc";
            PageHelper.startPage(page, size, orderBy);
            LambdaQueryWrapper<UserLike> lqw = new LambdaQueryWrapper<>();
            lqw.eq(UserLike::getUserId, userId);
            List<UserLike> userLikeList = userLikeMapper.selectList(lqw);
            PageInfo pageInfo = new PageInfo<>(userLikeList);
            List<SimpleBookVO> books = new ArrayList<>();
            for (UserLike userLike : userLikeList) {
                String bookId = userLike.getBookId();
                Book book = bookService.getBookById(bookId).getData();
                SimpleBookVO simpleBookVO = new SimpleBookVO();
                BeanUtils.copyProperties(book,simpleBookVO);
                books.add(simpleBookVO);
            }
            pageInfo.setList(books);
            return ResultUtil.success(pageInfo);
        } catch (Exception ex) {
            log.error("获取用户[{}]喜欢书单异常：{}", userId, ex);
            return ResultUtil.fail();
        }
    }

    @Override
    public Result userLikeThisBook(Integer userId, String bookId) {
        return null;
    }
}
