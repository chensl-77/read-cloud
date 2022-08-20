package top.csl.read;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.csl.read.common.utils.RedisUtil;

import java.time.Duration;

/**
 * @Author: csl
 * @DateTime: 2022/8/14 18:36
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public void test1(){
//        redisUtil.setKey("aaa","2",100L);
//        stringRedisTemplate.opsForValue().set("key","value",100000);
//        System.out.println(stringRedisTemplate.opsForValue().get("key"));
//        stringRedisTemplate.opsForValue().set("csl","1");
//        stringRedisTemplate.opsForValue().set("cslss","1",1000000);
        redisUtil.setKey("abc","2",redisUtil.getSecondsNextEarlyMorning());
    }
}
