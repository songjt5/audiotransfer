package com.cmos.audiotransfer.taskmanager.config;

import com.cmos.audiotransfer.taskmanager.handlers.ResourceConsumer;
import com.cmos.audiotransfer.taskmanager.handlers.SendMessageProducer;
import com.cmos.audiotransfer.taskmanager.handlers.TaskLocate;
import com.cmos.audiotransfer.taskmanager.weights.WeightConfigs;
import com.cmos.audiotransfer.taskmanager.weights.WeightManager;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;


@Configuration public class TaskManagerConfig {

    Logger logger = LoggerFactory.getLogger(TaskManagerConfig.class);

    @Bean public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer =
            new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new FileSystemResource(
            TaskManagerConfig.class.getResource("/").getPath() + "config/weights.yml"));
        configurer.setProperties(yaml.getObject());
        return configurer;
    }

    @Bean public WeightManager weightManager(WeightConfigs weightConfigs) {
        WeightManager weightManager = new WeightManager();
        weightManager.init(weightConfigs);
        return weightManager;
    }

    @Bean public ResourceConsumer resourceConsumer(WeightManager weightManager, TaskLocate locator,
        SendMessageProducer sendMessageProducer) {
        ResourceConsumer resourceConsumer =
            new ResourceConsumer(weightManager, locator, sendMessageProducer);
        try {
            resourceConsumer.init();
        } catch (MQClientException e) {
            logger.error("Resource Consumer Init Failed!", e);
            return null;
        }
        return resourceConsumer;
    }
}
