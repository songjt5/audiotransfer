package com.cmos.audiotransfer.taskgroup;


import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.taskgroup.constant.GroupStatusConsts;
import com.cmos.audiotransfer.taskgroup.filters.FilterManager;
import com.cmos.audiotransfer.taskgroup.handler.StatusProducer;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.taskgroup.manager.ChannelManager;
import com.cmos.audiotransfer.taskgroup.util.RedisOperator;
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
    private ChannelManager channelManager;


    public TaskConsumer(FilterManager filterManager, RedisOperator redisOperator,
        StatusProducer statusProducer, ChannelManager channelManager) {
        this.filterManager = filterManager;
        this.redisOperator = redisOperator;
        this.statusProducer = statusProducer;
        this.channelManager = channelManager;
    }

    @KafkaListener(topics = "consumer_task_origin") public void processMessage(String content) {
        Map<String, String> taskInfo = JSONUtil.toMap(content);
        TaskBean task;
        if (taskInfo == null || CollectionUtils.isEmpty(taskInfo)) {
            task = new TaskBean();
            task.setStatus(GroupStatusConsts.TASK_STATUS_MSGPARSEERROR);
            task.setDetail("message parse failed:" + content);
            logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, content);
        } else {
            task = TaskBean.createTaskBean(taskInfo);
            if (StringUtils.isEmpty(task.getChannelId())) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("channel id is null!" + content);
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            } else if (channelManager
                .invalidChannelId(taskInfo.get(ConfigConsts.TASK_CHANNELID_ORIGIN))) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_INVALIDCHANNELID);
                task.setDetail("invalid channeld!" + content);
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            } else if (StringUtils.isEmpty(task.getId())) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("task id is null!" + content);
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            } else if (StringUtils.isEmpty(task.getPath())) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("path is null!" + content);
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            } else if (task.getBeginTime() == null) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("begin time is null!" + content);
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            } else if (task.getEndTime() == null) {
                task.setStatus(GroupStatusConsts.TASK_STATUS_MISSKEY);
                task.setDetail("end time is null!" + content);
                logger.error(GroupStatusConsts.TASK_STATUS_GROUPFAILED, task);
            } else {
                taskInfo.put(ConfigConsts.TASK_CHANNELID,
                    taskInfo.get(ConfigConsts.TASK_CHANNELID_ORIGIN));
                taskInfo.put(ConfigConsts.TASK_ID, taskInfo.get(ConfigConsts.TASK_ID_ORIGIN));
                taskInfo.put(ConfigConsts.TASK_RADIO_PATH,
                    taskInfo.get(ConfigConsts.TASK_RADIO_PATH_ORIGIN));
                taskInfo.put(ConfigConsts.TASK_TIME_BEGIN,
                    taskInfo.get(ConfigConsts.TASK_TIME_BEGIN_ORIGIN));
                taskInfo.put(ConfigConsts.TASK_TIME_END,
                    taskInfo.get(ConfigConsts.TASK_TIME_END_ORIGIN));

                try {

                    String redisListKey = filterManager.getRedisKey(taskInfo);
                    redisOperator.putTask(redisListKey, JSONUtil.toJSON(task));
                    task.setStatus(GroupStatusConsts.TASK_STATUS_GROUPED);

                } catch (Exception e) {
                    logger.error("转写任务分组失败:" + JSONUtil.toJSON(task), e);
                    task.failCountInc();
                    task.setStatus(GroupStatusConsts.TASK_STATUS_REDISCONECTFAILED);
                    task.setDetail(e.getMessage());
                    statusProducer.send(task);
                }
            }
        }
        statusProducer.send(task);
    }

}
