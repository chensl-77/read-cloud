package top.csl.read;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;
import top.csl.read.common.constant.RabbitMQConstant;
import top.csl.read.common.utils.DateUtil;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: csl
 * @DateTime: 2022/8/15 18:03
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private DateUtil dateUtil;


    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test1(){
        String dayStr = dateUtil.dateFormatA();
        Long result = redisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                dayStr, "24"
        );
        int r = result.intValue();
        System.out.println(r);
    }

    @Test
    public void test2(){
        Map<String, Object> map = getMap(24);
        rabbitTemplate.convertAndSend(RabbitMQConstant.TOPIC_EXCHANGE_NAME,"sign.csl", map);
    }

    /**
     * 创建RabbitMQ消息对象
     * @param msg
     * @return
     */
    private Map<String, Object> getMap(Integer msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", msg);
        return map;
    }
}
