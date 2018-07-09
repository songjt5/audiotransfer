package com.cmos.audiotransfer.common.exception;

import com.google.gson.annotations.Expose;

public class Task {

    /*任务唯一id*/
    @Expose
    private String id;

    /*接触记录id*/
    @Expose
    private String contactRecordId;

    /*渠道id*/
    @Expose
    private String channelId;

    /*语音数据源id*/
    @Expose
    private String sourceId;


    /*语音路径*/
    @Expose
    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactRecordId() {
        return contactRecordId;
    }

    public void setContactRecordId(String contactRecordId) {
        this.contactRecordId = contactRecordId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
