package com.cmos.audiotransfer.taskgroup.filters;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component @ConfigurationProperties(prefix = "configs") public class FilterConfigs {

    /*filter配置信息*/
    private List<Map<String, String>> filterList;

    public List<Map<String, String>> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<Map<String, String>> filterList) {
        this.filterList = filterList;
    }

}
