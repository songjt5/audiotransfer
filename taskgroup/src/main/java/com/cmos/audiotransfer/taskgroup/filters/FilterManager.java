package com.cmos.audiotransfer.taskgroup.filters;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.taskgroup.manager.TaskCacheManager;
import com.cmos.audiotransfer.common.constant.TaskPriority;
import io.netty.handler.stream.ChunkedNioFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterManager {
    private static final Logger logger = LoggerFactory.getLogger(FilterManager.class);
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


    public boolean addFilter(Filter filter) {
        ArrayList<Filter> channelFilters = (ArrayList<Filter>) filters.get(filter.getChannel());
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(channelFilters)) {
            channelFilters = new ArrayList<>();
            channelFilters.add(filter);
        } else {
            int length = channelFilters.size();
            channelFilters = (ArrayList<Filter>) channelFilters.clone();
            int i;
            for (i = 0; i < channelFilters.size(); i++) {
                try {
                    if (Integer.parseInt(channelFilters.get(i).priortyValue.getValue()) < Integer
                        .parseInt(filter.getPriortyValue().getValue())) {
                        channelFilters.add(i, filter);
                        break;
                    }
                } catch (NumberFormatException e) {
                    logger.error("illegal priority!", e);
                    return false;
                }
            }
            if (i == length) {
                channelFilters.add(i, filter);
            }

        }
        filters.put(filter.getChannel(), channelFilters);
        return true;
    }

    public boolean deleteFilter(String id, String channelId) {
        ArrayList<Filter> channelFilters = (ArrayList<Filter>) filters.get(channelId);
        channelFilters = (ArrayList<Filter>) channelFilters.clone();
        if (channelFilters == null) {
            logger.error("invalid channel id: " + channelId);
            return false;
        } else {
            if (channelFilters.remove(id)) {

                filters.put(channelId, channelFilters);
            } else {
                return false;
            }
        }
        return true;
    }

    public List<Filter> queryFilters(String channelId) {
        if (StringUtils.isEmpty(channelId)) {
            return null;
        } else {
            return filters.get(channelId.trim());
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

