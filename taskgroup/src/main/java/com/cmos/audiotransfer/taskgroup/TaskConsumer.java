package com.cmos.audiotransfer.taskgroup;


import com.cmos.audiotransfer.common.beans.TaskBean;
import com.cmos.audiotransfer.taskgroup.constant.GroupStatusConsts;
import com.cmos.audiotransfer.taskgroup.filters.FilterManager;
import com.cmos.audiotransfer.taskgroup.handlers.StatusProducer;
import com.cmos.audiotransfer.common.utils.ConfigKey;
import com.cmos.audiotransfer.common.utils.JSONUtil;
import com.cmos.audiotransfer.taskgroup.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public class TaskConsumer {
    private Logger logger = LoggerFactory.getLogger(TaskConsumer.class);

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
        Map<String, String> taskInfo = JSONUtil.toMap(content);
        TaskBean task;
        if (taskInfo == null || CollectionUtils.isEmpty(taskInfo)) {
            task = new TaskBean();
            task.setStatus(GroupStatusConsts.TASK_STATUS_MSGPARSEERROR);
            task.setDetail("message parse failed:" + content);
            logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, content);
        } else {
            task = new TaskBean(taskInfo);
            if (StringUtils.isEmpty(taskInfo.get(ConfigKey.CHANNEL_ID))) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("channel id is null!");
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            }
            if (StringUtils.isEmpty(taskInfo.get(ConfigKey.TASK_ID))) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("task id is null!");
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            }
            if (StringUtils.isEmpty(taskInfo.get(ConfigKey.TASK_RADIO_PATH))) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("path is null!");
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            }
            if (StringUtils.isEmpty(taskInfo.get(ConfigKey.TASK_TIME))) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("time is null!");
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            }
            try {
                String redisListKey = filterManager.getRedisKey(taskInfo);
                redisOperator.putTask(redisListKey, content);
                task.setStatus(GroupStatusConsts.TASK_STATUS_GROUPED);

            } catch (Exception e) {
                logger.error("转写任务分组失败:" + JSONUtil.toJSON(task), e);
                task.setStatus(GroupStatusConsts.TASK_STATUS_REDISCONECTFAILED);
                task.setDetail(e.getMessage());
                statusProducer.send(taskInfo, content);
            }
        }
        statusProducer.send(taskInfo, JSONUtil.toJSON(task));
    }

}
