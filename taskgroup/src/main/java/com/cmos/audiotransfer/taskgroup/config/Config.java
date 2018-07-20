package com.cmos.audiotransfer.taskgroup.config;

import com.cmos.audiotransfer.taskgroup.TaskConsumer;
import com.cmos.audiotransfer.taskgroup.filters.FilterConfigs;
import com.cmos.audiotransfer.taskgroup.filters.FilterFactory;
import com.cmos.audiotransfer.taskgroup.filters.FilterManager;
import com.cmos.audiotransfer.taskgroup.handler.StatusProducer;
import com.cmos.audiotransfer.taskgroup.handler.TaskHandler;
import com.cmos.audiotransfer.taskgroup.manager.ChannelManager;
import com.cmos.audiotransfer.taskgroup.manager.TaskCacheManager;
import com.cmos.audiotransfer.taskgroup.util.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.net.URISyntaxException;

@Configuration public class Config {


    @Autowired RedisClusterConfig redisConfig;
    Logger logger = LoggerFactory.getLogger(Config.class);

    @Bean public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer =
            new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        try {
            yaml.setResources(new FileSystemResource(
                Config.class.getResource("/").toURI().getPath() + "config/filters.yml"));
        } catch (URISyntaxException e) {
            logger.error("URL is illegal", e);
        }
        configurer.setProperties(yaml.getObject());
        return configurer;
    }

  /*  @Bean public ReactiveRedisConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
        config.setPassword(RedisPassword.of("pelerin12345"));
        return new LettuceConnectionFactory(config);
        //return new LettuceConnectionFactory(new RedisClusterConfiguration(redisConfig.getNodes()));
    }

    @Bean ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
        ReactiveRedisConnectionFactory lettuceConnectionFactory) {
        return new ReactiveRedisTemplate<>(lettuceConnectionFactory,
            RedisSerializationContext.string());
    }*/

    @Bean FilterManager filterManager(FilterFactory filterFactory, FilterConfigs filterConfig,
        TaskCacheManager cacheManager) {

        return new FilterManager(filterFactory.getFilters(filterFactory, filterConfig),
            cacheManager);
    }

    @Bean TaskConsumer kafkaConsumerConfig(FilterManager filterManager, RedisOperator redisOperator,
        StatusProducer statusProducer, ChannelManager channelManager) {
        return new TaskConsumer(filterManager, redisOperator, statusProducer, channelManager);
    }

    @Bean TaskHandler taskHandler(FilterManager filter) {
        return new TaskHandler(filter);
    }



}
