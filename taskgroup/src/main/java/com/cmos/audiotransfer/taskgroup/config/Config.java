package com.cmos.audiotransfer.taskgroup.config;

import com.cmos.audiotransfer.taskgroup.TaskConsumer;
import com.cmos.audiotransfer.taskgroup.filters.FilterConfigs;
import com.cmos.audiotransfer.taskgroup.filters.FilterFactory;
import com.cmos.audiotransfer.taskgroup.filters.FilterManager;
import com.cmos.audiotransfer.taskgroup.handlers.StatusProducer;
import com.cmos.audiotransfer.taskgroup.handlers.TaskHandler;
import com.cmos.audiotransfer.taskgroup.manager.TaskCacheManager;
import com.cmos.audiotransfer.taskgroup.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

@Configuration public class Config {


    @Autowired RedisClusterConfig redisConfig;
    Logger logger = LoggerFactory.getLogger(Config.class);

    @Bean public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer =
            new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(
            new FileSystemResource(Config.class.getResource("/").getPath() + "config/filters.yml"));
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
        StatusProducer statusProducer) {
        return new TaskConsumer(filterManager, redisOperator, statusProducer);
    }

    @Bean TaskHandler taskHandler(FilterManager filter) {
        return new TaskHandler(filter);
    }



}
