package com.cmos.audiotransfer.taskmanager.weights;

import com.cmos.audiotransfer.taskmanager.beans.ChannelWeightBean;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component @ConfigurationProperties(prefix = "weights") public class WeightConfigs {


    private Logger logger = LoggerFactory.getLogger(WeightConfigs.class);

    /*filter配置信息*/
    private List<Map<String, String>> weightList;

    public List<Map<String, String>> getWeightList() {
        return weightList;
    }

    public void setWeightList(List<Map<String, String>> weightList) {
        this.weightList = weightList;
    }

    public List<ChannelWeightBean> getWeightConfigs() {
        if (CollectionUtils.isEmpty(weightList))
            return null;
        return weightList.stream().map(p -> {
            try {
                return new ChannelWeightBean(p);
            } catch (Exception e) {
                logger.error("weight init failed!:" + p.toString(), e);
                return null;
            }

        }).filter(p -> p != null).collect(Collectors.toList());
    }
}
