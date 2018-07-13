package com.cmos.audiotransfer.taskgroup.handler;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.taskgroup.filters.FilterManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TaskHandler {

    private FilterManager filter;


    private Logger logger = LoggerFactory.getLogger(TaskHandler.class);

    public TaskHandler(FilterManager filter) {
        this.filter = filter;
    }

    public TaskHandler checkInfo(Map<String, String> msg) {

        if (StringUtils.isEmpty(msg.get(ConfigConsts.TASK_CHANNELID_ORIGIN)) || StringUtils
            .isEmpty(msg.get(ConfigConsts.TASK_ID_ORIGIN)) || StringUtils
            .isEmpty(msg.get(ConfigConsts.TASK_RADIO_PATH_ORIGIN)) || StringUtils
            .isEmpty(msg.get(ConfigConsts.TASK_TIME_BEGIN_ORIGIN)) || StringUtils
            .isEmpty(msg.get(ConfigConsts.TASK_TIME_END_ORIGIN))) {
            logger.error("necessary column is null!");
        }

        return this;

    }
}
