package top.csl.read;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.csl.read.client.BookService;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: csl
 * @DateTime: 2022/8/15 19:20
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private BookService bookService;

    @Test
    public void test1(){
        Map<String, Object> map = getMap(24);
        bookService.savedayuser((Integer) map.get("userId"));
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
