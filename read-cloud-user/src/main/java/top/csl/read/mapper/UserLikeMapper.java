package top.csl.read.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.csl.read.common.pojo.account.UserLike;

/**
 * @Author: csl
 * @DateTime: 2022/8/11 11:05
 **/
@Mapper
@Repository
public interface UserLikeMapper extends BaseMapper<UserLike> {
}
