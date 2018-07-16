package com.cmos.audiotransfer.taskmanager.handlers;

import com.cmos.audiotransfer.common.bean.TransformResource;
import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.bean.CachedTask;
import com.cmos.audiotransfer.common.constant.TopicConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.taskmanager.handlers.degrade.TaskDegradeManager;
import com.cmos.audiotransfer.taskmanager.weights.WeightManager;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResourceConsumer {
    Logger logger = LoggerFactory.getLogger(ResourceConsumer.class);

    private MQConsumer consumer;

    private WeightManager weightManager;

    private TaskQueueManager taskQueueManager;

    private DispachStatusProducer sender;

    private TaskDegradeManager degradeManager;

    public ResourceConsumer() {

    }

    public ResourceConsumer(WeightManager weightManager, TaskQueueManager taskQueueManager,
        DispachStatusProducer sender, TaskDegradeManager degradeManager) {
        this.weightManager = weightManager;
        this.taskQueueManager = taskQueueManager;
        this.sender = sender;
        this.degradeManager = degradeManager;
    }

    public ResourceConsumer init() throws MQClientException {
        DefaultMQPushConsumer consumer =
            new DefaultMQPushConsumer(TopicConsts.TOPIC_TRANSFER_RESOURCE);
        consumer.subscribe(TopicConsts.TOPIC_TRANSFER_RESOURCE, "*");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //consumer.setConsumeTimestamp("20170422221800");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            String resourceStr = new String(msgs.get(0).getBody());
            TransformResource resource = JSONUtil.fromJson(resourceStr, TransformResource.class);
            if (resource == null) {
                logger.error("illegal resource string!", resourceStr);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            CachedTask taskCacheObj = getTaskCacheBean(resource);
            if (taskCacheObj == null) {
                sender.resumeWithNoTask(resource);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            TaskBean task = JSONUtil.fromJson(taskCacheObj.getTaskStr(), TaskBean.class);
            task.setResourceId(resource.getResourceId());
            task.setLastRecoverTime(resource.getLastRecoverTime());
            if (!sender.dispach(task)) {
                taskQueueManager.pushBack(taskCacheObj);
                sender.resumeWithTask(task);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

        });
        consumer.start();
        logger.info("Resource Consumer Started.");
        return this;
    }

    private CachedTask getTaskCacheBean(TransformResource resource) {
        String channel = weightManager.getChannel(resource.getTypeCode());
        CachedTask taskCacheObj = this.taskQueueManager.popOrderedTask(channel);
        if (taskCacheObj == null) {
            List<String> channels = weightManager.getReflectChannelList(resource.getTypeCode());
            for (String channelId : channels) {
                taskCacheObj = this.taskQueueManager.popOrderedTask(channelId);
                if (taskCacheObj != null) {
                    break;
                }
            }
        }
        return taskCacheObj;
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
