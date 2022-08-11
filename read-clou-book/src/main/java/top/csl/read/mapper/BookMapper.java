package top.csl.read.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.csl.read.common.pojo.book.Book;

/**
 * @Author: csl
 * @DateTime: 2022/8/11 10:27
 **/
@Mapper
@Repository
public interface BookMapper extends BaseMapper<Book> {
}
