package com.cmos.audiotransfer.transfermanager.event;

import com.cmos.audiotransfer.common.bean.TaskBean;

/**
 * Created by hejie
 * Date: 18-7-26
 * Time: 下午2:25
 * Description:
 */
public class TransformEvent {

    private TaskBean task;
    private String localPath;
    private String xml;
    private String json;

    public TaskBean getTask() {
        return task;
    }

    public void setTask(TaskBean task) {
        this.task = task;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
