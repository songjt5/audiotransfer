package com.cmos.audiotransfer.transfermanager.service;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.transfermanager.event.TransformEvent;
import com.cmos.audiotransfer.transfermanager.event.TransformEventTranslator;
import com.cmos.audiotransfer.transfermanager.handler.*;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by hejie
 * Date: 18-7-23
 * Time: 下午7:35
 * Description:
 */
public class TaskTransformManager {

    private Disruptor<TransformEvent> disruptor;


    public void init() {

        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        final int bufferSize = 1024;//用来指定ring buffer的大小，必须是2的倍数
        disruptor =
            new Disruptor<>(TransformEvent::new, bufferSize, threadFactory, ProducerType.SINGLE,
                new BlockingWaitStrategy());

        disruptor.handleEventsWith(new DownLoadHandler()).then(new DecoderHandler())
            .then(new TransformHandler()).then(new ResultFormatHandler()).then(new UploadHandler());
        disruptor.start();
    }


    public void publicEvent(TaskBean task) {
        disruptor.publishEvent(new TransformEventTranslator(), task);
    }

}
