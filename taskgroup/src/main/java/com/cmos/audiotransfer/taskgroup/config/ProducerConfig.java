package com.cmos.audiotransfer.taskgroup.config;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.util.LocalHostInfo;
import com.cmos.audiotransfer.taskgroup.handler.StatusProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration public class ProducerConfig {
    Logger logger = LoggerFactory.getLogger(ProducerConfig.class);

    @Value("${spring.rocketmq.nameserver}") private String nameServer;

    @Bean StatusProducer statusProducer(LocalHostInfo hostInfo) {
        DefaultMQProducer producer = new DefaultMQProducer(ConfigConsts.TASK_STATUS_PRODUCER_GROUP);
        producer.setNamesrvAddr(nameServer);
        StatusProducer statProducer = new StatusProducer();
        statProducer.setProducer(producer);
        statProducer.setHostInfo(hostInfo);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("producer init failed!", e);
        }
        return statProducer;
    }
}
