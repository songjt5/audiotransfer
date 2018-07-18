package com.cmos.audiotransfer.taskmanager.beans;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.taskmanager.constant.DispatchConfigConsts;

import java.util.Map;

/*
 * 渠道号权重配置实体*/
public class ChannelWeightBean {

    //渠道编号
    String channelId;

    //权重
    Integer weight;

    //所用资源编号
    String resourceCode;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public ChannelWeightBean() {

    }


    public ChannelWeightBean(Map<String, String> beanInfo) {
        this.channelId = beanInfo.get(ConfigConsts.TASK_CHANNELID).trim();
        if (!beanInfo.containsKey(DispatchConfigConsts.DISPATCH_CHANNEL_WEIGHT)) {
            this.weight = 5;
        } else {
            this.weight =
                Integer.parseInt(beanInfo.get(DispatchConfigConsts.DISPATCH_CHANNEL_WEIGHT));
        }
        this.resourceCode = beanInfo.get(ConfigConsts.TASK_RESOURCE_CODE).trim();
    }
}
