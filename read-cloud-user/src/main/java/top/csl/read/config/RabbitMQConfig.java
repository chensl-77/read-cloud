package top.csl.read.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.csl.read.common.constant.RabbitMQConstant;

import javax.annotation.Resource;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 19:34
 **/
@Component
@Configuration
public class RabbitMQConfig implements BeanPostProcessor {

    //这是创建交换机和队列用的rabbitAdmin对象
    @Resource
    private RabbitAdmin rabbitAdmin;

    //初始化rabbitAdmin对象
    @Bean
    @SuppressWarnings("All")
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    //实例化bean后，也就是Bean的后置处理器
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //创建交换机
        rabbitAdmin.declareExchange(rabbitmqDemoTopicExchange());
        rabbitAdmin.declareExchange(lindtopicExchange());
        //创建队列
        rabbitAdmin.declareQueue(topicExchangeQueueSign());
        rabbitAdmin.declareQueue(lindtopicExchangeQueue());
        return null;
    }


    @Bean
    public TopicExchange rabbitmqDemoTopicExchange() {
        //配置TopicExchange交换机
        return new TopicExchange(RabbitMQConstant.TOPIC_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue topicExchangeQueueSign() {
        //创建sign队列
        return QueueBuilder.durable(RabbitMQConstant.TOPIC_EXCHANGE_QUEUE_SIGN)
                .withArgument("x-dead-letter-exchange", RabbitMQConstant.LIND_DL_EXCHANGE)//设置死信交换机
//              .withArgument("x-message-ttl", 10000)
                .withArgument("x-dead-letter-routing-key", "sixin.queue")//设置死信routingKey
                .build();
    }

    @Bean
    public Binding bindTopicSign() {
        //sign队列绑定到topicExchange交换机
        return BindingBuilder.bind(topicExchangeQueueSign())
                .to(rabbitmqDemoTopicExchange())
                .with("sign.*");
    }

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public TopicExchange lindtopicExchange() {
        //创建死信交换机 同一个项目的死信交换机可以共用一个，然后为每个业务队列分配一个单独的路由key。
        return (TopicExchange) ExchangeBuilder.topicExchange(RabbitMQConstant.LIND_DL_EXCHANGE).durable(true)
                .build();
    }

    @Bean
    public Queue lindtopicExchangeQueue() {
        //创建死信队列
        return new Queue(RabbitMQConstant.LIND_TOPIC_EXCHANGE_QUEUE, true, false, false);
    }

    @Bean
    public Binding bindLindTopisign(){
        //sixin队列绑定到lindtopicExchange交换机
        return BindingBuilder.bind(lindtopicExchangeQueue())
                .to(lindtopicExchange())
                .with("sixin.queue");
    }
}
