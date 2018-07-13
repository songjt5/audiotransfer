package com.cmos.audiotransfer.taskgroup.handler;


import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.constant.MQTagConsts;
import com.cmos.audiotransfer.common.constant.TopicConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.common.util.LocalHostInfo;
import com.cmos.audiotransfer.taskgroup.constant.GroupStatusConsts;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class StatusProducer {

    private MQProducer producer;

    private LocalHostInfo hostInfo;

    private Logger logger = LoggerFactory.getLogger(StatusProducer.class);

    public MQProducer getProducer() {
        return producer;
    }

    public void setProducer(MQProducer producer) {
        this.producer = producer;
    }

    public boolean send(TaskBean task) {
        Message msg;
        task.setGroupTime(new Date());
        task.setDetail(
            new StringBuilder(task.getDetail()).append(hostInfo.getLocalInfo()).toString());
        if (GroupStatusConsts.TASK_STATUS_GROUPED.equals(task.getStatus())) {
            msg = new Message(TopicConsts.TOPIC_TASK_STATUS, MQTagConsts.TAG_MESSAGE_GROUPED,
                JSONUtil.toJSON(task).getBytes());
        } else {
            msg = new Message(TopicConsts.TOPIC_TASK_STATUS, MQTagConsts.TAG_MESSAGE_GROUPFAILED,
                JSONUtil.toJSON(task).getBytes());
        }
        try {
            SendResult sendResult = producer.send(msg);
            if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                return true;
            } else {
                logger.error("task grouped msg send failed,send status is exception");
                return false;
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            logger.error("task grouped msg send failed!", e);
            return false;
        }

    }

    public LocalHostInfo getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(LocalHostInfo hostInfo) {
        this.hostInfo = hostInfo;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
