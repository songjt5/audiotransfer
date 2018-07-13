package com.cmos.audiotransfer.taskmanager.handlers;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.constant.MQTagConsts;
import com.cmos.audiotransfer.common.constant.TopicConsts;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMessageProducer {

    private MQProducer producer;

    private Logger logger = LoggerFactory.getLogger(SendMessageProducer.class);

    public MQProducer getProducer() {
        return producer;
    }

    public void setProducer(MQProducer producer) {
        this.producer = producer;
    }

    public boolean sendStatus(TaskBean task, String content) {

        Message msg = new Message(TopicConsts.TOPIC_TASK_STATUS, MQTagConsts.TAG_TASK_SENT,
            new StringBuilder(task.getChannelId()).append(task.getId()).toString(),
            content.getBytes());
        return send(msg);
    }

    public boolean send(Message msg) {
        SendResult sendResult = null;
        try {
            sendResult = producer.send(msg);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            logger.error("task sent msg send failed!", e);
            return false;
        }
        if (sendResult.getSendStatus().equals(SendStatus.SEND_OK))
            return true;
        else {
            logger.error("task sent msg send failed!", new String(msg.getBody()));
            return false;
        }
    }


    public boolean sendTask(TaskBean task, String content) {

        Message msg = new Message(TopicConsts.TOPIC_TASK_SENT, MQTagConsts.TAG_TASK_SENT,
            new StringBuilder(task.getChannelId()).append(task.getId()).toString(),
            content.getBytes());
        return send(msg);
    }

}
