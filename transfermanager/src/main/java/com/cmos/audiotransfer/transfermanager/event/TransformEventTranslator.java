package com.cmos.audiotransfer.transfermanager.event;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * Created by hejie
 * Date: 18-7-31
 * Time: 下午2:17
 * Description:
 */
public class TransformEventTranslator implements EventTranslatorOneArg<TransformEvent, TaskBean> {

    @Override public void translateTo(TransformEvent transformEvent, long l, TaskBean task) {
        transformEvent.setTask(task);
    }
}
