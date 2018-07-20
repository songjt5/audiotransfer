package com.cmos.audiotransfer.taskgroup.filters;

import com.cmos.audiotransfer.common.constant.TaskPriority;
import org.springframework.util.StringUtils;

import java.util.Map;

public abstract class Filter {

    /*filterId*/
    private String id;
    /*渠道号*/
    private String channel;

    /*字段关键字*/
    protected String key;

    /*优先级*/
    protected TaskPriority priortyValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TaskPriority getPriortyValue() {
        return priortyValue;
    }

    public void setPriortyValue(TaskPriority priortyValue) {
        this.priortyValue = priortyValue;
    }


    public TaskPriority apply(Map<String, String> taskInfo) {
        String value = taskInfo.get(key);
        if (StringUtils.isEmpty(value))
            return TaskPriority.DEFAULT;
        return getPriority(value);
    }

    protected abstract TaskPriority getPriority(String value);


}
