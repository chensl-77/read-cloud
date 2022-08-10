package top.csl.read.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.csl.read.common.pojo.account.User;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 12:44
 **/
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    User selectByLoginName(String loginName);
}
