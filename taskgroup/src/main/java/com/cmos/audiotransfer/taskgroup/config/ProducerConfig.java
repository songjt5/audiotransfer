package com.cmos.audiotransfer.taskgroup.config;

import com.cmos.audiotransfer.taskgroup.handlers.StatusProducer;
import com.cmos.audiotransfer.taskgroup.utils.ConfigKey;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {
    Logger logger = LoggerFactory.getLogger(ProducerConfig.class);

    @Value("${spring.rocketmq.nameserver}") private String nameServer;
    @Bean StatusProducer statusProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(ConfigKey.TASK_STATUS_TOPIC);
        producer.setNamesrvAddr(nameServer);
        StatusProducer statProducer = new StatusProducer();
        statProducer.setProducer(producer);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("producer init failed!", e);
        }
        return statProducer;
    }
}
