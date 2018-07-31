package com.cmos.audiotransfer.transfermanager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hejie
 * Date: 18-7-31
 * Time: 上午9:16
 * Description:
 */
@Component @ConfigurationProperties(prefix = "result-json") public class JsonResultConfigs {
    private static final Logger logger = LoggerFactory.getLogger(IsaEngineConfigs.class);

    private String insightType;
    private String taskInsightField;
    private String dataSource;
    private String dataType;
    private String dimensionType;
    private String speechPlatform = "default";

    private List<Map<String, String>> dimensionList;

    private Map<String, Map<String, String>> dimensionFieldIndexInfo;


    @PostConstruct public void init() {

        try {
            dimensionFieldIndexInfo = dimensionList.stream()
                .collect(Collectors.toMap(p -> p.get("originalName"), p -> p));
        } catch (Exception e) {
            logger.error("the json result dimension config is invalid!");
        }
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

    public String getSpeechPlatform() {
        return speechPlatform;
    }

    public void setSpeechPlatform(String speechPlatform) {
        this.speechPlatform = speechPlatform;
    }

    public List<Map<String, String>> getDimensionList() {
        return dimensionList;
    }

    public void setDimentionList(List<Map<String, String>> dimensionList) {
        this.dimensionList = dimensionList;
    }

    public Map<String, Map<String, String>> getDimensionFieldIndexInfo() {
        return dimensionFieldIndexInfo;
    }

    public void setDimensionFieldIndexInfo(
        Map<String, Map<String, String>> dimensionFieldIndexInfo) {
        this.dimensionFieldIndexInfo = dimensionFieldIndexInfo;
    }
}
