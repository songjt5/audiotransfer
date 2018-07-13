package com.cmos.audiotransfer.taskmanager.handlers;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.constant.TaskPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component public class TaskQueueManager {
    @Autowired RedisTemplate redisTemplate;

    public String popOrderedTask(String channelId) {
        for (TaskPriority priority : TaskPriority.values()) {
            String content = (String) redisTemplate.opsForList().rightPop(
                new StringBuilder(ConfigConsts.TASK_QUEUE_KEY_PREFIX).append(channelId).append("_")
                    .append(priority).toString());
            if (content != null)
                return content;
        }
        return null;
    }

    public String popRecentTask(String channelId) {
        for (TaskPriority priority : TaskPriority.values()) {
            String content = (String) redisTemplate.opsForList().leftPop(
                new StringBuilder(ConfigConsts.TASK_QUEUE_KEY_PREFIX).append(channelId).append("_")
                    .append(priority).toString());
            if (content != null)
                return content;
        }
        return null;
    }

    public void pushBack(TaskBean task){


    }
}
