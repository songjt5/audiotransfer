package com.cmos.audiotransfer.taskmanager.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskLocate {
    @Autowired RedisTemplate redisTemplate;

    public String getOrderedTask(String channelId) {
        for (int i = 5; i >= 0; i--) {
            String content = (String) redisTemplate.opsForList().rightPop(
                new StringBuilder("task_").append(channelId).append("_").append(i).toString());
            if (content != null)
                return content;
        }
        return null;
    }

    public String getRecentTask(String channelId) {
        for (int i = 5; i >= 0; i--) {
            String content = (String) redisTemplate.opsForList().leftPop(
                new StringBuilder("task_").append(channelId).append("_").append(i).toString());
            if (content != null)
                return content;
        }
        return null;
    }
}
