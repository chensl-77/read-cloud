package top.csl.read.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.csl.read.common.pojo.book.DayBook;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 11:43
 **/
@Mapper
@Repository
public interface DayBookMapper extends BaseMapper<DayBook> {
}
