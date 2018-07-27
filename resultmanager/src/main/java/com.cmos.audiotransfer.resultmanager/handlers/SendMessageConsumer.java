package com.cmos.audiotransfer.resultmanager.handlers;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.constant.TopicConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.resultmanager.Dao.ResultMongoDB;
import com.cmos.audiotransfer.resultmanager.ResultProducers;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMessageConsumer {
    Logger logger = LoggerFactory.getLogger(SendMessageConsumer.class);
    private MQConsumer consumer;
    private ResultProducers sender;
    private ResultMongoDB resultMongoDB;

    public SendMessageConsumer(){

    }

    public SendMessageConsumer init() throws MQClientException{
        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer(TopicConsts.TOPIC_TRANSFER_RESOURCE);
        //订阅一个topic下的所有消息 就是从哪个会话取值
        consumer.subscribe("AAAASSSS", "*");
        //consumer.subscribe(TopicConsts.TOPIC_TRANSFER_RESOURCE, "*");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) ->{
            String msg = new String(msgs.get(0).getBody());
            if (msg == null) {
                logger.error("illegal resource string!", msg);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            TaskBean taskInfo = JSONUtil.fromJson(msg, TaskBean.class);
//            Map<String, String> taskInfo = JSONUtil.toMap(msg);
            if(null != taskInfo){
                resultMongoDB.SaveMes(taskInfo);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        logger.info("consumer begin stared");
        return this;
    }

    public MQConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(MQConsumer consumer) {
        this.consumer = consumer;
    }

    public ResultProducers getSender() {
        return sender;
    }

    public void setSender(ResultProducers sender) {
        this.sender = sender;
    }

    public ResultMongoDB getResultMongoDB() {
        return resultMongoDB;
    }

    public void setResultMongoDB(ResultMongoDB resultMongoDB) {
        this.resultMongoDB = resultMongoDB;
    }

}
