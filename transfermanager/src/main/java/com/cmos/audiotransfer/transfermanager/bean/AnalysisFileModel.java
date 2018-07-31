package com.cmos.audiotransfer.transfermanager.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-27
 * Time: 上午11:05
 * Description:
 */
public class AnalysisFileModel {

    //数据源
    private String dataSource;

    //0：录音 1：文本
    private String dataType;

    //0：离线 1：实时
    private String dimensionType;


    //分析类型：（0：按录音，1：按任务）
    private String insightType;

    //如为按任务分析，则表示按任务分析字段域
    private String taskInsightField;

    //语音文件数量
    private int voiceSize;

    //平台标识
    private String speechPlatform;

    //录音维度信息列表（电话信息维度数据和引擎结果维度数据）
    private List<Map<String, String>> dimValueList;

    //索引建索域标识
    private Map<String, Map<String, String>> dimensionFieldIndexInfo;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(String dimensionType) {
        this.dimensionType = dimensionType;
    }

    public String getInsightType() {
        return insightType;
    }

    public void setInsightType(String insightType) {
        this.insightType = insightType;
    }

    public String getTaskInsightField() {
        return taskInsightField;
    }

    public void setTaskInsightField(String taskInsightField) {
        this.taskInsightField = taskInsightField;
    }

    public int getVoiceSize() {
        return voiceSize;
    }

    public void setVoiceSize(int voiceSize) {
        this.voiceSize = voiceSize;
    }

    public String getSpeechPlatform() {
        return speechPlatform;
    }

    public void setSpeechPlatform(String speechPlatform) {
        this.speechPlatform = speechPlatform;
    }

    public List<Map<String, String>> getDimValueList() {
        return dimValueList;
    }

    public void setDimValueList(List<Map<String, String>> dimValueList) {
        this.dimValueList = dimValueList;
    }

    public Map<String, Map<String, String>> getDimensionFieldIndexInfo() {
        return dimensionFieldIndexInfo;
    }

    public void setDimensionFieldIndexInfo(
        Map<String, Map<String, String>> dimensionFieldIndexInfo) {
        this.dimensionFieldIndexInfo = dimensionFieldIndexInfo;
    }

    @Override public String toString() {
        return "[批次结果]AnalysisFileModel{" + "dataSource='" + dataSource + '\'' + ", dataType="
            + dataType + ", dimensionType=" + dimensionType + ", insightType=" + insightType
            + ", taskInsightField='" + taskInsightField + '\'' + ", voiceSize=" + voiceSize
            + ", speechPlatform='" + speechPlatform + '\'' + ", dimValueList=" + dimValueList
            + ", dimensionFieldIndexInfo=" + dimensionFieldIndexInfo + '}';
    }
}
