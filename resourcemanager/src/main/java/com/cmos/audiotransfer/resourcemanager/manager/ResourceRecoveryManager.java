package com.cmos.audiotransfer.resourcemanager.manager;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.bean.TransformResource;
import com.cmos.audiotransfer.common.constant.MQTagConsts;
import com.cmos.audiotransfer.common.constant.TopicConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hejie
 * Date: 18-7-16
 * Time: 下午6:29
 * Description:
 */
public class ResourceRecoveryManager {

    private Logger logger = LoggerFactory.getLogger(ResourceRecoveryManager.class);

    private ResourceInfoManager resourceInfoManager;

    private ResourceCapacityManager resourceCapacityManager;

    public void init() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(TopicConsts.TOPIC_TASK_STATUS);
        consumer.subscribe(TopicConsts.TOPIC_TASK_STATUS,
            new StringBuilder(MQTagConsts.TAG_RESOURCE_NO_TASK).append(" || ")
                .append(MQTagConsts.TAG_TASK_DISPACHFAILED).append(" || ")
                .append(MQTagConsts.TAG_TRANSFORM_FAILED).append(" || ")
                .append(MQTagConsts.TAG_TRANSFORMED).toString());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //consumer.setConsumeTimestamp("20170422221800");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {

            String dataStr = new String(msgs.get(0).getBody());
            String tag = msgs.get(0).getTags();
            TransformResource resource;
            if (tag.equals(MQTagConsts.TAG_RESOURCE_NO_TASK)) {
                resource = JSONUtil.fromJson(dataStr, TransformResource.class);
            } else {
                TaskBean task = JSONUtil.fromJson(dataStr, TaskBean.class);
                TransformResource newResource = new TransformResource();
                String[] idArray = task.getResourceId().split("_");
                newResource.setInnerCode(Integer.parseInt(idArray[1]));
                newResource.setTypeCode(idArray[0]);
                newResource.setLastRecoverTime(task.getLastRecoverTime());
                resource = newResource;
            }
            if (resourceCapacityManager
                .identifyInvalidResource(resource.getTypeCode(), resource.getInnerCode())) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            if (resourceInfoManager.recoverResource(resource, tag, dataStr)) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }


        });
        consumer.start();
        logger.info("Resource Consumer Started.");
    }

    public ResourceInfoManager getResourceInfoManager() {
        return resourceInfoManager;
    }

    public void setResourceInfoManager(ResourceInfoManager resourceInfoManager) {
        this.resourceInfoManager = resourceInfoManager;
    }

    public ResourceCapacityManager getResourceCapacityManager() {
        return resourceCapacityManager;
    }

    public void setResourceCapacityManager(ResourceCapacityManager resourceCapacityManager) {
        this.resourceCapacityManager = resourceCapacityManager;
    }
}
