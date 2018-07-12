package com.cmos.audiotransfer.taskmanager.handlers;

import com.cmos.audiotransfer.common.beans.ResourceBean;
import com.cmos.audiotransfer.common.beans.TaskBean;
import com.cmos.audiotransfer.common.utils.ConfigKey;
import com.cmos.audiotransfer.common.utils.JSONUtil;
import com.cmos.audiotransfer.taskmanager.weights.WeightManager;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

public class ResourceConsumer {
    Logger logger = LoggerFactory.getLogger(ResourceConsumer.class);

    private MQConsumer consumer;

    private WeightManager weightManager;

    private TaskLocate locator;

    private SendMessageProducer sender;

    public ResourceConsumer() {

    }

    public ResourceConsumer(WeightManager weightManager, TaskLocate locator,
        SendMessageProducer sender) {
        this.weightManager = weightManager;
        this.locator = locator;
        this.sender = sender;
    }

    public ResourceConsumer init() throws MQClientException {
        DefaultMQPushConsumer consumer =
            new DefaultMQPushConsumer(ConfigKey.CONSUMER_GROUP_RESOURCE);
        consumer.subscribe(ConfigKey.RESOURCE_TOPIC, "*");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //consumer.setConsumeTimestamp("20170422221800");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            String content = new String(msgs.get(0).getBody());
            System.out.println("get Resource" + content);
            ResourceBean resource = JSONUtil.fromJson(content, ResourceBean.class);
            System.out.println(resource);
            String channel = weightManager.getChannel(resource.getTypeCode());
            String taskStr = this.locator.getOrderedTask(channel);
            if (taskStr == null) {
                List<String> channels = weightManager.getReflectChannelList(resource.getTypeCode());
                for (String channelId : channels) {
                    taskStr = this.locator.getOrderedTask(channelId);
                    if (taskStr != null)
                        break;
                }

            }
            if (taskStr == null)
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            System.out.println("gettask" + taskStr);
            TaskBean task = JSONUtil.fromJson(taskStr, TaskBean.class);
            sender.sendTask(task, taskStr);
            sender.sendStatus(task,taskStr);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        logger.info("Resource Consumer Started.");
        return this;
    }


    public MQConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(MQConsumer consumer) {
        this.consumer = consumer;
    }

    public WeightManager getWeightManager() {
        return weightManager;
    }

    public void setWeightManager(WeightManager weightManager) {
        this.weightManager = weightManager;
    }
}
