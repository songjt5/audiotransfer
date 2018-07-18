package com.cmos.audiotransfer.resourcemanager.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-18
 * Time: 上午9:19
 * Description:
 */
@Component @ConfigurationProperties(prefix = "capacity-config")
public class ResourceCapacityManager {

    private Logger logger = LoggerFactory.getLogger(ResourceCapacityManager.class);

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
}
