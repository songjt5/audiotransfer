package com.cmos.audiotransfer.common.bean;

/**
 * Created by hejie
 * Date: 18-7-13
 * Time: 下午5:54
 * Description:
 */
public class CachedTask {

    String taskStr;

    String redisQueueKey;

    public CachedTask(String taskStr, String redisQueueKey) {
        this.taskStr = taskStr;
        this.redisQueueKey = redisQueueKey;
    }

    public String getTaskStr() {
        return taskStr;
    }

    public void setTaskStr(String taskStr) {
        this.taskStr = taskStr;
    }

    public String getRedisQueueKey() {
        return redisQueueKey;
    }

    public void setRedisQueueKey(String redisQueueKey) {
        this.redisQueueKey = redisQueueKey;
    }
}
