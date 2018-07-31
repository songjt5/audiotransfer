package com.cmos.audiotransfer.transfermanager.config;

import com.cmos.audiotransfer.transfermanager.isa.ISAEngine;
import com.cmos.audiotransfer.transfermanager.isa.IsaEnginePool;
import com.cmos.audiotransfer.transfermanager.isa.IsaFactory;
import com.cmos.audiotransfer.transfermanager.service.TaskTransformManager;
import com.cmos.audiotransfer.transfermanager.service.TransferTaskConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.net.URISyntaxException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by hejie
 * Date: 18-7-23
 * Time: 下午7:25
 * Description:
 */
@Configuration public class TransformerManagerConfig {
    private static final Logger logger = LoggerFactory.getLogger(TransformerManagerConfig.class);

    @Bean public IsaEnginePool isaEnginePool(IsaEngineConfigs engineConfig, IsaFactory isaFactory,
        IsaEngineConfigs configs) {

        IsaEnginePool enginePool = new IsaEnginePool<ISAEngine>();
        BlockingQueue<ISAEngine> poolEntity = null;
        try {
            Integer maxEngine = Integer.parseInt(engineConfig.getMaxEngine());

            if (maxEngine < 1) {
                logger.error("the max num of isa engine is less than 1");
                return null;
            }
            poolEntity = new ArrayBlockingQueue<>(maxEngine);
            for (int i = 0; i < maxEngine; i++) {
                ISAEngine engine = isaFactory.getNewEngine();
                if (engine.Initialize()) {
                    poolEntity.add(engine);
                } else {
                    throw new RuntimeException("isa engine init failed");
                }
            }
        } catch (NumberFormatException e) {
            logger.error("the max num of isa engine is illegal");
            return null;
        }
        enginePool.setObjects(poolEntity);


        return enginePool;
    }

    @Bean public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer =
            new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        try {
            yaml.setResources(new FileSystemResource(
                TransformerManagerConfig.class.getResource("/").toURI().getPath()
                    + "config/transformConfig.yml"));
        } catch (URISyntaxException e) {
            logger.error("URL is illegal", e);
        }
        configurer.setProperties(yaml.getObject());
        return configurer;
    }

    @Bean public TaskTransformManager taskTransformManager() {
        TaskTransformManager taskTransformManager = new TaskTransformManager();
        taskTransformManager.init();
        return taskTransformManager;
    }

    @Bean
    public TransferTaskConsumer TransferTaskConsumer(TaskTransformManager taskTransformManager) {
        TransferTaskConsumer transferTaskConsumer = new TransferTaskConsumer();
        transferTaskConsumer.setTaskTransformManager(taskTransformManager);
        try {
            transferTaskConsumer.init();
        } catch (MQClientException e) {
            logger.error("transferTaskConsumer init failed!", e);
            System.exit(1);
        }
        return transferTaskConsumer;
    }

}
