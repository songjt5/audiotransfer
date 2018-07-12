package com.cmos.audiotransfer.taskmanager.handlers;

import com.cmos.audiotransfer.common.beans.TaskBean;
import com.cmos.audiotransfer.common.utils.ConfigKey;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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

        Message msg = new Message(ConfigKey.TASK_STATUS_TOPIC, ConfigKey.TASK_STATUS_SENT,
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

        Message msg = new Message(ConfigKey.TOPIC_TASK_SENT, ConfigKey.TASK_STATUS_SENT,
            new StringBuilder(task.getChannelId()).append(task.getId()).toString(),
            content.getBytes());
        return send(msg);
    }

}
