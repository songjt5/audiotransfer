package com.cmos.audiotransfer.resourcemanager.manager;

import com.cmos.audiotransfer.common.bean.TransformResource;
import com.cmos.audiotransfer.common.constant.MQTagConsts;
import com.cmos.audiotransfer.common.util.JSONUtil;
import com.cmos.audiotransfer.resourcemanager.handler.ResourceProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created by hejie
 * Date: 18-7-18
 * Time: 上午9:19
 * Description:
 */
@Component @ConfigurationProperties(prefix = "capacity-config")
public class ResourceCapacityManager {

    private Logger logger = LoggerFactory.getLogger(ResourceCapacityManager.class);

    @Autowired ResourceProducer resourceProducer;

    public Map<String, Integer> getCapacities() {
        return capacities;
    }

    public void setCapacities(Map<String, Integer> capacities) {
        this.capacities = capacities;
    }

    /*filter配置信息*/
    private Map<String, Integer> capacities;

    public boolean identifyInvalidResource(String resourceType, int innerCode) {
        if (capacities.get(resourceType) != null && innerCode < capacities.get(resourceType)) {
            return false;
        }
        return true;
    }


    public Set<String> resetResourceEntities(Set<String> exclusiveKeys) {
        capacities.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                if (exclusiveKeys.contains(new StringBuilder(k).append("_").append(i).toString())) {
                    continue;
                }
                TransformResource newResource = new TransformResource();
                newResource.setTypeCode(k);
                newResource.setInnerCode(i);
                if (resourceProducer.resumeToStatusTopic(JSONUtil.toJSON(newResource),
                    MQTagConsts.TAG_RESOURCE_NO_TASK)) {
                    continue;
                } else {
                    exclusiveKeys.add(new StringBuilder(k).append("_").append(i).toString());
                }
            }
        });

        return exclusiveKeys;
    }
}
