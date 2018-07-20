package com.cmos.audiotransfer.taskmanager.handlers;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.bean.TransformResource;
import com.cmos.audiotransfer.common.constant.MQTagConsts;
import com.cmos.audiotransfer.common.constant.TopicConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.taskmanager.constant.DispatchStatusConsts;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DispatchStatusProducer {

    private MQProducer producer;

    private Logger logger = LoggerFactory.getLogger(DispatchStatusProducer.class);

    public MQProducer getProducer() {
        return producer;
    }

    public void setProducer(MQProducer producer) {
        this.producer = producer;
    }

    public boolean dispatch(TaskBean task) {

        task.setDispatchTime(new Date());
        task.setStatus(DispatchStatusConsts.TASK_STATUS_REDISCONECTFAILED);
        Message msg = new Message(TopicConsts.TOPIC_TASK_STATUS, MQTagConsts.TAG_TASK_DISPATCHED,
            JSONUtil.toJSON(task).getBytes());
        return send(msg);
    }

    public boolean send(Message msg) {
        try {
            producer.send(msg);
            return true;
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            logger.error(
                new StringBuilder((new String(msg.getBody()))).append("task sent msg send failed!")
                    .toString(), e);
            return false;
        }

    }

    public boolean resumeWithTask(TaskBean task) {

        Message msg =
            new Message(TopicConsts.TOPIC_TASK_STATUS, MQTagConsts.TAG_TASK_DISPATCHFAILED,
                JSONUtil.toJSON(task).getBytes());
        return send(msg);
    }

    public boolean resumeWithNoTask(TransformResource resource) {

        Message msg = new Message(TopicConsts.TOPIC_TASK_STATUS, MQTagConsts.TAG_RESOURCE_NO_TASK,
            JSONUtil.toJSON(resource).getBytes());
        return send(msg);
    }

}
