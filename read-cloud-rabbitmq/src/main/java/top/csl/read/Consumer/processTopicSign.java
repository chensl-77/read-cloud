package top.csl.read.Consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.csl.read.client.BookService;
import top.csl.read.common.constant.RabbitMQConstant;

import java.io.IOException;
import java.util.Map;


@Slf4j
@Component
@RabbitListener(queuesToDeclare = @Queue(RabbitMQConstant.TOPIC_EXCHANGE_QUEUE_SIGN))
public class processTopicSign {

    @Autowired
    private BookService bookService;


    public static int num = 0;

    enum Action {
        //处理成功
        SUCCESS,
        //可以重试的错误，消息重回队列
        RETRY,
        //无需重试的错误，拒绝消息，并从队列中删除
        REJECT
    }

    @RabbitHandler
    public void processTopicSign(Map map, Channel channel, Message message) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        Action action = Action.SUCCESS;
        try {
            log.info("消费者从TOPIC_EXCHANGE_QUEUE_SIGN消费消息:{}",map);
            bookService.savedayuser((Integer) map.get("userId"));
        }catch (Exception e) {
            //打印异常
            log.error("处理消息异常：{},消息：{}",e,map);
            //根据异常的类型判断，设置action是可重试的，还是无需重试的
            action = Action.RETRY;
        } finally {
            try {
                if (action == Action.SUCCESS) {
                    //multiple 表示是否批量处理。true表示批量ack处理小于tag的所有消息。false则处理当前消息
                    channel.basicAck(tag, false);
                } else {
                    //Nack，拒绝策略，消息重回队列
                    if (num++ > 5){
                        channel.basicNack(tag, false, false);
                    }else {
                        channel.basicNack(tag, false, true);
                    }
                }
                channel.close();
            } catch (Exception e) {
                log.error("finally处理消息异常：{},消息：{}",e,map);
            }
        }
    }



}
