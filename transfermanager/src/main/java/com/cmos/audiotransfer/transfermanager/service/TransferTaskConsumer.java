package com.cmos.audiotransfer.transfermanager.service;

import com.cmos.audiotransfer.common.bean.CachedTask;
import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.bean.TransformResource;
import com.cmos.audiotransfer.common.constant.MQGroupConsts;
import com.cmos.audiotransfer.common.constant.MQTagConsts;
import com.cmos.audiotransfer.common.constant.TopicConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hejie
 * Date: 18-7-23
 * Time: 下午7:26
 * Description:
 */
public class TransferTaskConsumer {

    Logger logger = LoggerFactory.getLogger(TransferTaskConsumer.class);
    private MQConsumer consumer;
    private TaskTrasnformManager TaskTrasnformManager;


    public TransferTaskConsumer init() throws MQClientException {
        DefaultMQPushConsumer consumer =
            new DefaultMQPushConsumer(MQGroupConsts.GROUP_CONSUMER_TASK_DISPATCHED);
        consumer.subscribe(TopicConsts.TOPIC_TRANSFER_RESOURCE, MQTagConsts.TAG_TASK_DISPATCHED);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //consumer.setConsumeTimestamp("20170422221800");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            String taskStr = new String(msgs.get(0).getBody());
            TaskBean task = JSONUtil.fromJson(taskStr, TaskBean.class);
            if (task == null) {
                logger.error("illegal task string!", taskStr);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }


            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

        });
        consumer.start();
        logger.info("Resource Consumer Started.");
        return this;
    }
}
