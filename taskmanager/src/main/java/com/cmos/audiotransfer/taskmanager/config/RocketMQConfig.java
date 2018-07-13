package com.cmos.audiotransfer.taskmanager.config;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.taskmanager.handlers.DispachStatusProducer;
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

    @Bean DispachStatusProducer sendStatusProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(ConfigConsts.TASK_STATUS_PRODUCER_GROUP);
        producer.setNamesrvAddr(nameServer);
        DispachStatusProducer statProducer = new DispachStatusProducer();
        statProducer.setProducer(producer);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("producer init failed!", e);
        }
        return statProducer;
    }
}
