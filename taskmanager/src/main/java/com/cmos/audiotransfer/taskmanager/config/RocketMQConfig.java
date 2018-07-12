package com.cmos.audiotransfer.taskmanager.config;

import com.cmos.audiotransfer.common.utils.ConfigKey;
import com.cmos.audiotransfer.taskmanager.handlers.SendMessageProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConfig {

    Logger logger = LoggerFactory.getLogger(RocketMQConfig.class);

    @Value("${spring.rocketmq.nameserver}") private String nameServer;
    @Bean SendMessageProducer sendStatusProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(ConfigKey.TASK_STATUS_TOPIC);
        producer.setNamesrvAddr(nameServer);
        SendMessageProducer statProducer = new SendMessageProducer();
        statProducer.setProducer(producer);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("producer init failed!", e);
        }
        return statProducer;
    }
}
