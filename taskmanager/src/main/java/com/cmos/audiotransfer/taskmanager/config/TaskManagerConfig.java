package com.cmos.audiotransfer.taskmanager.config;

import com.cmos.audiotransfer.taskmanager.handlers.ResourceConsumer;
import com.cmos.audiotransfer.taskmanager.handlers.DispachStatusProducer;
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

import java.net.URISyntaxException;


@Configuration public class TaskManagerConfig {

    Logger logger = LoggerFactory.getLogger(TaskManagerConfig.class);

    @Bean public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer =
            new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        try {
            yaml.setResources(new FileSystemResource(
                TaskManagerConfig.class.getResource("/").toURI().getPath() + "config/weights.yml"));
        } catch (URISyntaxException e) {
            logger.error("URL is illegal", e);
        }
        configurer.setProperties(yaml.getObject());
        return configurer;
    }

    @Bean public WeightManager weightManager(WeightConfigs weightConfigs) {
        WeightManager weightManager = new WeightManager();
        weightManager.init(weightConfigs);
        return weightManager;
    }

    @Bean public ResourceConsumer resourceConsumer(WeightManager weightManager, TaskLocate locator,
        DispachStatusProducer dispachStatusProducer) {
        ResourceConsumer resourceConsumer =
            new ResourceConsumer(weightManager, locator, dispachStatusProducer);
        try {
            resourceConsumer.init();
        } catch (MQClientException e) {
            logger.error("Resource Consumer Init Failed!", e);
            return null;
        }
        return resourceConsumer;
    }
}
