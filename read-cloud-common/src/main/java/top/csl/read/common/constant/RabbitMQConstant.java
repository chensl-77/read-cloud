package top.csl.read.common.constant;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 18:41
 **/
public class RabbitMQConstant {

    /**
     * RabbitMQ的TOPIC_EXCHANGE交换机名称
     */
    public static final String TOPIC_EXCHANGE_NAME = "topic.exchange.name";

    /**
     * RabbitMQ的TOPIC_EXCHANGE交换机的队列A的名称
     */
    public static final String TOPIC_EXCHANGE_QUEUE_SIGN = "topic.queue.sign";

    /**
     * RabbitMQ的TOPIC_EXCHANGE死信交换机名称
     */
    public static final String LIND_DL_EXCHANGE = "topic.lind.exchange.name";

    /**
     * RabbitMQ的TOPIC_EXCHANGE死信队列的名称
     */
    public static final String LIND_TOPIC_EXCHANGE_QUEUE = "lind_topic.queue";


}
