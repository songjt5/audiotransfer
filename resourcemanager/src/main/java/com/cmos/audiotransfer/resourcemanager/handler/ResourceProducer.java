package com.cmos.audiotransfer.resourcemanager.handler;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.bean.TransformResource;
import com.cmos.audiotransfer.common.constant.MQTagConsts;
import com.cmos.audiotransfer.common.constant.TopicConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class ResourceProducer {

    private MQProducer producer;

    private Logger logger = LoggerFactory.getLogger(ResourceProducer.class);

    public MQProducer getProducer() {
        return producer;
    }

    public void setProducer(MQProducer producer) {
        this.producer = producer;
    }

    public boolean sendResourceMsg(TransformResource resource) {

        Message msg =
            new Message(TopicConsts.TOPIC_TRANSFER_RESOURCE, JSONUtil.toJSON(resource).getBytes());
        return send(msg);
    }

    private boolean send(Message msg) {
        try {
            producer.send(msg);
            return true;
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            logger.error(
                new StringBuilder((new String(msg.getBody()))).append("message put failed:")
                    .append(msg.getTopic()).toString(), e);
            return false;
        }

    }

    public boolean resumeToStatusTopic(String dataSte, String tags) {

        Message msg = new Message(TopicConsts.TOPIC_TASK_STATUS, tags,
            dataSte.getBytes());
        return send(msg);
    }

    public boolean resumeWithNoTask(TransformResource resource) {

        Message msg = new Message(TopicConsts.TOPIC_TASK_STATUS, MQTagConsts.TAG_RESOURCE_NO_TASK,
            JSONUtil.toJSON(resource).getBytes());
        return send(msg);
    }

}
