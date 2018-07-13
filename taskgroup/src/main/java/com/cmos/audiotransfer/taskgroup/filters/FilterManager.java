package com.cmos.audiotransfer.taskgroup.filters;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.taskgroup.manager.TaskCacheManager;
import com.cmos.audiotransfer.common.constant.TaskPriority;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class FilterManager {
    private Map<String, List<Filter>> filters;
    private TaskCacheManager cacheManager;


    public FilterManager(Map<String, List<Filter>> filters, TaskCacheManager cacheManager) {
        this.filters = filters;
        this.cacheManager = cacheManager;
    }


    public List<Filter> getFiltersByChannel(String channelId) {
        return this.filters.get(channelId);
    }

    public String getRedisKey(Map<String, String> taskInfo) {

        String channelId = taskInfo.get(ConfigConsts.TASK_CHANNELID).trim();

        List<Filter> channelFilters = getFiltersByChannel(channelId);
        if (CollectionUtils.isEmpty(channelFilters)) {
            return cacheManager.getDefaultKeys(channelId);
        } else {
            TaskPriority matchedPriority;
            for (Filter filter : channelFilters) {
                matchedPriority = filter.apply(taskInfo);
                if (TaskPriority.DEFAULT.equals(matchedPriority))
                    continue;
                else
                    return new StringBuilder(cacheManager.getDefaultKeys(channelId)).append("_")
                        .append(matchedPriority.getValue()).toString();
            }
            return cacheManager.getDefaultKeys(channelId);
        }
    }

    public Map<String, List<Filter>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<Filter>> filters) {
        this.filters = filters;
    }

    public TaskCacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(TaskCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}

