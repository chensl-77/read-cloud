package top.csl.read.Consumer;

import com.rabbitmq.client.Channel;
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

@Component
@RabbitListener(queuesToDeclare = @Queue(RabbitMQConstant.LIND_TOPIC_EXCHANGE_QUEUE))
public class processSixin {


    @Autowired
    private BookService bookService;

    @RabbitHandler
    public void processSixin(Map map, Channel channel, Message message) throws IOException {
        System.out.println("processSixin"+map.toString());
        bookService.savedayuser((Integer) map.get("userId"));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
