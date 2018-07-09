package com.cmos.audiotransfer.taskgroup.handlers;


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

public class StatusProducer {

    private MQProducer producer;

    private Logger logger = LoggerFactory.getLogger(StatusProducer.class);

    public MQProducer getProducer() {
        return producer;
    }

    public void setProducer(MQProducer producer) {
        this.producer = producer;
    }

    public boolean send(Map<String, String> taskInfo, String content) {

        Message msg = new Message(ConfigKey.TASK_STATUS_TOPIC, ConfigKey.TASK_STATUS_GROUPED,
            new StringBuilder(taskInfo.get(ConfigKey.CHANNEL_ID))
                .append(taskInfo.get(ConfigKey.TASK_ID)).toString(), content.getBytes());
        SendResult sendResult = null;
        try {
            sendResult = producer.send(msg);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            logger.error("task grouped msg send failed!", e);
            return false;
        }
        if (sendResult.getSendStatus().equals(SendStatus.SEND_OK))
            return true;
        else {
            logger.error("task grouped msg send failed!");
            return false;
        }


    }
}
