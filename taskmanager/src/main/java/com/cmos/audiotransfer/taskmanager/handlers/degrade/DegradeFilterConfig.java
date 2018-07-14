package com.cmos.audiotransfer.taskmanager.handlers.degrade;

import com.cmos.audiotransfer.taskmanager.weights.WeightConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-14
 * Time: 下午2:24
 * Description:
 */
@Component @ConfigurationProperties(prefix = "degradeFilters") public class DegradeFilterConfig {

    private Logger logger = LoggerFactory.getLogger(DegradeFilterConfig.class);

    public List<Map<String, String>> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<Map<String, String>> filterList) {
        this.filterList = filterList;
    }

    /*filter配置信息*/
    private List<Map<String, String>> filterList;
}
