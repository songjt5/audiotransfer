package com.cmos.audiotransfer.common.beans;

import com.cmos.audiotransfer.common.utils.ConfigKey;
import com.cmos.audiotransfer.common.utils.DateUtil;
import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.Map;

/*
 * 转写任务实体*/
public class TaskBean {

    @Expose private String id;

    @Expose private String channelId;

    @Expose private Date date;

    @Expose private String path;

    @Expose private String content;

    @Expose private String status;
    
    @Expose private String detail;

    @Expose private String xmlResult;

    @Expose private String jsonResult;

    public TaskBean() {

    }


    public TaskBean(Map<String, String> taskInfo) {
        this.id = taskInfo.get(ConfigKey.TASK_ID);
        this.channelId = taskInfo.get(ConfigKey.CHANNEL_ID);
        this.date = DateUtil.stringToDate(taskInfo.get(ConfigKey.TASK_TIME));
        this.path = taskInfo.get(ConfigKey.TASK_RADIO_PATH);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getXmlResult() {
        return xmlResult;
    }

    public void setXmlResult(String xmlResult) {
        this.xmlResult = xmlResult;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    public static void createTaskBean(TaskBean task, Map<String, String> content) {

        task.setId(content.get(ConfigKey.TASK_ID));

        task.setChannelId(content.get(ConfigKey.CHANNEL_ID));

        task.setDate(DateUtil.stringToDate(content.get(ConfigKey.CHANNEL_ID)));

        task.setPath(content.get(ConfigKey.TASK_TIME));

    }
}
