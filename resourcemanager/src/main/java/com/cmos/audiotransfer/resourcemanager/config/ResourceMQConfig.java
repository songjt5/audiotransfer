package com.cmos.audiotransfer.resourcemanager.config;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.constant.MQGroupConsts;
import com.cmos.audiotransfer.resourcemanager.handler.ResourceProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hejie
 * Date: 18-7-17
 * Time: 下午5:01
 * Description:
 */
@Configuration public class ResourceMQConfig {
    Logger logger = LoggerFactory.getLogger(ResourceMQConfig.class);

    @Value("${spring.rocketmq.nameserver}") private String nameServer;

    @Bean ResourceProducer resourceProducer() {
        DefaultMQProducer producer =
            new DefaultMQProducer(MQGroupConsts.GROUP_PRODUCER_RESOURCE_FRESH);
        producer.setNamesrvAddr(nameServer);
        ResourceProducer resourceProducer = new ResourceProducer();
        resourceProducer.setProducer(producer);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("producer init failed!", e);
        }
        return resourceProducer;
    }
}

