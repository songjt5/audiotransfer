package com.cmos.audiotransfer.taskgroup.handlers;

import com.cmos.audiotransfer.taskgroup.filters.FilterManager;
import com.cmos.audiotransfer.common.utils.ConfigKey;
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

        if (msg.get(ConfigKey.CHANNEL_ID).isEmpty() || msg.get(ConfigKey.TASK_ID).isEmpty() || msg
            .get(ConfigKey.TASK_RADIO_PATH).isEmpty() || msg.get(ConfigKey.TASK_TIME).isEmpty()) {
            logger.error("necessary column is null!");
        }

        return this;

    }
}
