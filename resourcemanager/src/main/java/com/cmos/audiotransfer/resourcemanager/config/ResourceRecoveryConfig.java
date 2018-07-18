package com.cmos.audiotransfer.resourcemanager.config;

import com.cmos.audiotransfer.resourcemanager.manager.ResourceCapacityManager;
import com.cmos.audiotransfer.resourcemanager.manager.ResourceInfoManager;
import com.cmos.audiotransfer.resourcemanager.manager.ResourceRecoveryManager;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.net.URISyntaxException;

/**
 * Created by hejie
 * Date: 18-7-16
 * Time: 下午6:32
 * Description:
 */
@Configuration public class ResourceRecoveryConfig {
    Logger logger = LoggerFactory.getLogger(ResourceRecoveryManager.class);

    @Bean ResourceRecoveryManager resourceRecoveryManager(ResourceInfoManager resourceInfoManager,
        ResourceCapacityManager resourceCapacityManager) {
        ResourceRecoveryManager resourceRecoveryManager = new ResourceRecoveryManager();
        resourceRecoveryManager.setResourceInfoManager(resourceInfoManager);
        resourceRecoveryManager.setResourceCapacityManager(resourceCapacityManager);
        try {
            resourceRecoveryManager.init();
        } catch (MQClientException e) {
            logger.error("resourceRecoveryManager init failed!", e);
            System.exit(1);
        }
        return resourceRecoveryManager;
    }

    @Bean public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer =
            new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        try {
            yaml.setResources(new FileSystemResource(
                ResourceRecoveryConfig.class.getResource("/").toURI().getPath()
                    + "config/capacityConfig.yml"));
        } catch (URISyntaxException e) {
            logger.error("URL is illegal", e);
            System.exit(1);
        }
        configurer.setProperties(yaml.getObject());
        return configurer;
    }

}
