package com.cmos.audiotransfer.taskmanager.handlers;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.bean.TaskCacheBean;
import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.constant.TaskPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component public class TaskQueueManager {

    Logger logger = LoggerFactory.getLogger(TaskQueueManager.class);
    @Autowired RedisTemplate redisTemplate;

    public TaskCacheBean popOrderedTask(String channelId) {
        String taskQueueKey;
        for (TaskPriority priority : TaskPriority.values()) {
            taskQueueKey =
                new StringBuilder(ConfigConsts.TASK_QUEUE_KEY_PREFIX).append(channelId).append("_")
                    .append(priority).toString();
            try {
                String taskStr = (String) redisTemplate.opsForList().rightPop(taskQueueKey);
                if (taskStr != null) {
                    return new TaskCacheBean(taskStr, taskQueueKey);
                }
            } catch (Exception e) {
                logger.error("get task failed,redis key is : " + taskQueueKey, e);
                return null;
            }
        }
        return null;
    }

    public TaskCacheBean popRecentTask(String channelId) {
        String taskQueueKey;
        for (TaskPriority priority : TaskPriority.values()) {
            taskQueueKey =
                new StringBuilder(ConfigConsts.TASK_QUEUE_KEY_PREFIX).append(channelId).append("_")
                    .append(priority).toString();
            try {
                String taskStr = (String) redisTemplate.opsForList().leftPop(taskQueueKey);
                if (taskStr != null)
                    return new TaskCacheBean(taskStr, taskQueueKey);
            } catch (Exception e) {
                logger.error("get task failed,redis key is : " + taskQueueKey, e);
                return null;
            }
        }
        return null;
    }

    public void pushBack(TaskCacheBean task) {
        try {
            redisTemplate.opsForList().leftPush(task.getRedisQueueKey(), task.getTaskStr());
        } catch (Exception e) {
            logger.error("task push back failed:" + task.getTaskStr(), e);
        }


    }
}
