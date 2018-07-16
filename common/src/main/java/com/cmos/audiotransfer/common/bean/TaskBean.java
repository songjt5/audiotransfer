package com.cmos.audiotransfer.common.bean;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.util.DateUtil;
import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.Map;

/*
 * 转写任务实体*/
public class TaskBean {
    /*流水号*/
    @Expose private String id;
    /*渠道号*/
    @Expose private String channelId;
    /*能力编码(分发状态)*/
    @Expose private String resourceCode;
    /*通话开始时间*/
    @Expose private Date beginTime;
    /*通话结束时间*/
    @Expose private Date endTime;
    /*分组完成时间(分组状态)*/
    @Expose private Date groupTime;
    /*分发完成时间(分发状态)*/
    @Expose private Date dispacheTime;
    /*转写开始时间(转写状态)*/
    @Expose private Date transferBeginTime;
    /*转写结束时间(转写状态)*/
    @Expose private Date transferEndTime;
    //任务执行的失败次数(所有)
    @Expose private Integer failCount;
    /*语音地址*/
    @Expose private String path;
    /*原始任务(分组状态)*/
    @Expose private String content;
    /*任务状态(所有)*/
    @Expose private String status;
    /*错误详情(所有)*/
    @Expose private String detail;
    /*xml转写结果地址(转写状态)*/
    @Expose private String xmlResult;
    /*json转写结果地址(转写状态)*/
    @Expose private String jsonResult;
    /*该任务分配的资源ID(分发状态,资源编码_资源编号)*/
    @Expose private String resourceId;
    /*该资源上次回收时间(资源上次回收时间yyyyMMddHHmmssSSS)*/
    @Expose private String lastRecoverTime;


    public TaskBean() {

    }


    public TaskBean(Map<String, String> taskInfo) {
        this.id = taskInfo.get(ConfigConsts.TASK_ID_ORIGIN);
        this.channelId = taskInfo.get(ConfigConsts.TASK_CHANNELID_ORIGIN);
        this.beginTime = DateUtil.stringToDate(taskInfo.get(ConfigConsts.TASK_TIME_BEGIN_ORIGIN));
        this.endTime = DateUtil.stringToDate(taskInfo.get(ConfigConsts.TASK_TIME_END_ORIGIN));
        this.path = taskInfo.get(ConfigConsts.TASK_RADIO_PATH_ORIGIN);
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

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(Date groupTime) {
        this.groupTime = groupTime;
    }

    public Date getDispacheTime() {
        return dispacheTime;
    }

    public void setDispacheTime(Date dispacheTime) {
        this.dispacheTime = dispacheTime;
    }

    public Date getTransferBeginTime() {
        return transferBeginTime;
    }

    public void setTransferBeginTime(Date transferBeginTime) {
        this.transferBeginTime = transferBeginTime;
    }

    public Date getTransferEndTime() {
        return transferEndTime;
    }

    public void setTransferEndTime(Date transferEndTime) {
        this.transferEndTime = transferEndTime;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
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

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getLastRecoverTime() {
        return lastRecoverTime;
    }

    public void setLastRecoverTime(String lastRecoverTime) {
        this.lastRecoverTime = lastRecoverTime;
    }

    public static TaskBean createTaskBean(Map<String, String> taskInfo) {

        TaskBean task = new TaskBean();
        task.setId(taskInfo.get(ConfigConsts.TASK_ID_ORIGIN));

        task.setChannelId(taskInfo.get(ConfigConsts.TASK_CHANNELID_ORIGIN));

        task.beginTime = DateUtil.stringToDate(taskInfo.get(ConfigConsts.TASK_TIME_BEGIN_ORIGIN));
        task.endTime = DateUtil.stringToDate(taskInfo.get(ConfigConsts.TASK_TIME_END_ORIGIN));

        task.setPath(taskInfo.get(ConfigConsts.TASK_RADIO_PATH_ORIGIN));
        return task;
    }

    public void failCountInc() {
        this.failCount = this.failCount.intValue() + 1;
    }
}
