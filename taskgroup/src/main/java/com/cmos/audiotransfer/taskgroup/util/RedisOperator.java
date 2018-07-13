package com.cmos.audiotransfer.taskgroup.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component public class RedisOperator {

    @Autowired RedisTemplate redisTemplate;


    public boolean putTask(String key, String taskMsg) {
        redisTemplate.opsForList().leftPush(key, taskMsg);
        return true;
    }
}
