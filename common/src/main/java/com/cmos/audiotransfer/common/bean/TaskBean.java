package com.cmos.audiotransfer.common.bean;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.util.DateUtils;
import com.google.gson.annotations.Expose;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

/*
 * 转写任务实体*/
@Document(collection = "task_status") public class TaskBean {
    /*流水号*/
    @Field("id")
    @Expose private String id;
    /*渠道号*/
    @Field("channel")
    @Expose private String channelId;
    /*通话开始时间*/
    @Field("start_t")
    @Expose private Date beginTime;
    /*通话结束时间*/
    @Field("end_t")
    @Expose private Date endTime;
    /*分组完成时间(分组状态)*/
    @Field("group_t")
    @Expose private Date groupTime;
    /*分发完成时间(分发状态)*/
    @Field("disp_t")
    @Expose private Date dispatchTime;
    /*转写开始时间(转写状态)*/
    @Field("trans_b_t")
    @Expose private Date transformBeginTime;
    /*转写结束时间(转写状态)*/
    @Field("trans_e_t")
    @Expose private Date transformEndTime;
    //任务执行的失败次数(所有)
    @Field("fail_count")
    @Expose private Integer failCount;
    /*语音地址*/
    @Field("path")
    @Expose private String path;
    /*原始任务(分组状态)*/
    @Field("t_origin")
    @Expose private String content;
    /*任务状态(所有)*/
    @Field("stat")
    @Expose private String status;
    /*错误详情(所有)*/
    @Field("detail")
    @Expose private String detail;
    /*xml转写结果地址(转写状态)*/
    @Field("xml")
    @Expose private String xmlResult;
    /*json转写结果地址(转写状态)*/
    @Field("json")
    @Expose private String jsonResult;
    /*该任务分配的资源ID(分发状态,资源编码_资源编号)*/
    @Field("resource")
    @Expose private String resourceId;
    /*该资源上次回收时间(资源上次回收时间戳)*/
    @Field("recover_t")
    @Expose private String lastRecoverTime;


    public TaskBean() {

    }


    public TaskBean(Map<String, String> taskInfo) {
        this.id = taskInfo.get(ConfigConsts.TASK_ID_ORIGIN);
        this.channelId = taskInfo.get(ConfigConsts.TASK_CHANNELID_ORIGIN);
        this.beginTime = DateUtils.stringToDate(taskInfo.get(ConfigConsts.TASK_TIME_BEGIN_ORIGIN));
        this.endTime = DateUtils.stringToDate(taskInfo.get(ConfigConsts.TASK_TIME_END_ORIGIN));
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

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public Date getTransformBeginTime() {
        return transformBeginTime;
    }

    public void setTransformBeginTime(Date transformBeginTime) {
        this.transformBeginTime = transformBeginTime;
    }

    public Date getTransformEndTime() {
        return transformEndTime;
    }

    public void setTransformEndTime(Date transformEndTime) {
        this.transformEndTime = transformEndTime;
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

        task.beginTime = DateUtils.stringToDate(taskInfo.get(ConfigConsts.TASK_TIME_BEGIN_ORIGIN));
        task.endTime = DateUtils.stringToDate(taskInfo.get(ConfigConsts.TASK_TIME_END_ORIGIN));

        task.setPath(taskInfo.get(ConfigConsts.TASK_RADIO_PATH_ORIGIN));
        return task;
    }

    public void failCountInc() {
        this.failCount = this.failCount.intValue() + 1;
    }
}
