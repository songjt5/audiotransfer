package com.cmos.audiotransfer.resultmanager.config;

import com.cmos.audiotransfer.resultmanager.handlers.SendMessageProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration public class RocketMQConfig {

    Logger logger = LoggerFactory.getLogger(RocketMQConfig.class);

    @Value("${spring.rocketmq.nameserver}") private String nameServer;

    @Bean SendMessageProducer sendMessageProducer() {
        //TopicConsts.TOPIC_TRANSFER_RESOURCE
        //定义一个producer group
        DefaultMQProducer producer = new DefaultMQProducer("AAAASSSS");
        producer.setNamesrvAddr(nameServer);
        SendMessageProducer messProducer = new SendMessageProducer();
        messProducer.setProducer(producer);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("producer init failed!", e);
        }
        return messProducer;
    }

}
