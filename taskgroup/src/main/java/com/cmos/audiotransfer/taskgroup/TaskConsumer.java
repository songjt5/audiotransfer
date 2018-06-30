package com.cmos.audiotransfer.taskgroup;


import com.cmos.audiotransfer.taskgroup.filters.FilterManager;
import com.cmos.audiotransfer.taskgroup.handlers.StatusProducer;
import com.cmos.audiotransfer.taskgroup.utils.ConfigKey;
import com.cmos.audiotransfer.taskgroup.utils.JSONUtil;
import com.cmos.audiotransfer.taskgroup.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

public class TaskConsumer {
    Logger logger = LoggerFactory.getLogger(TaskConsumer.class);

    private FilterManager filterManager;
    private RedisOperator redisOperator;
    private StatusProducer statusProducer;


    public TaskConsumer(FilterManager filterManager, RedisOperator redisOperator,
        StatusProducer statusProducer) {
        this.filterManager = filterManager;
        this.redisOperator = redisOperator;
        this.statusProducer = statusProducer;
    }

    @KafkaListener(topics = "task") public void processMessage(String content) {
        try {
            Map<String, String> taskInfo = JSONUtil.toMap(content);

            if (CollectionUtils.isEmpty(taskInfo))
                return;
            if (taskInfo.get(ConfigKey.CHANNEL_ID).isEmpty()) {
                logger.error("channel id is null!");
            }
            if (taskInfo.get(ConfigKey.TASK_ID).isEmpty()) {
                logger.error("task id is null!");
            }
            if (taskInfo.get(ConfigKey.TASK_RADIO_PATH).isEmpty()) {
                logger.error("path is null!");
            }
            if (taskInfo.get(ConfigKey.TASK_TIME).isEmpty()) {
                logger.error("time is null!");
            }
            String redisListKey = filterManager.getRedisKey(taskInfo);
            if (StringUtils.isEmpty(redisListKey))
                return;
            redisOperator.putTask(redisListKey, content);
            statusProducer.send(taskInfo, content);

        } catch (Exception e) {
            logger.error("转写任务数据验证处理失败:" + content, e);
        }

    }

}
