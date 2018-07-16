package com.cmos.audiotransfer.taskmanager.handlers;

import com.cmos.audiotransfer.common.bean.CachedTask;
import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.constant.TaskPriority;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.taskmanager.handlers.degrade.TaskDegradeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component public class TaskQueueManager {

    Logger logger = LoggerFactory.getLogger(TaskQueueManager.class);
    @Autowired RedisTemplate redisTemplate;

    @Autowired TaskDegradeManager degradeManager;

    public CachedTask popOrderedTask(String channelId) {
        String taskQueueKey;
        for (TaskPriority priority : TaskPriority.values()) {
            taskQueueKey =
                new StringBuilder(ConfigConsts.TASK_QUEUE_KEY_PREFIX).append(channelId).append("_")
                    .append(priority).toString();
            try {
                String taskStr = (String) redisTemplate.opsForList().rightPop(taskQueueKey);
                if (taskStr != null) {
                    TaskBean task = JSONUtil.fromJson(taskStr, TaskBean.class);
                    if (!TaskPriority.ZERO.equals(priority) && degradeManager.checkDegrade(task)) {
                        pushBack(new CachedTask(
                            new StringBuilder(ConfigConsts.TASK_QUEUE_KEY_PREFIX).append(channelId)
                                .append("_").append(TaskPriority.ZERO.getValue()).toString(),
                            taskStr));
                        logger.error("task degrade!", taskStr);
                    }
                    return new CachedTask(taskStr, taskQueueKey);
                }
            } catch (Exception e) {
                logger.error("get task failed,redis key is : " + taskQueueKey, e);
                return null;
            }
        }
        return null;
    }

    public CachedTask popRecentTask(String channelId) {
        String taskQueueKey;
        for (TaskPriority priority : TaskPriority.values()) {
            taskQueueKey =
                new StringBuilder(ConfigConsts.TASK_QUEUE_KEY_PREFIX).append(channelId).append("_")
                    .append(priority).toString();
            try {
                String taskStr = (String) redisTemplate.opsForList().leftPop(taskQueueKey);
                if (taskStr != null)
                    return new CachedTask(taskStr, taskQueueKey);
            } catch (Exception e) {
                logger.error("get task failed,redis key is : " + taskQueueKey, e);
                return null;
            }
        }
        return null;
    }

    public void pushBack(CachedTask task) {
        try {
            redisTemplate.opsForList().leftPush(task.getRedisQueueKey(), task.getTaskStr());
        } catch (Exception e) {
            logger.error("task push back failed:" + task.getTaskStr(), e);
        }


    }
}
