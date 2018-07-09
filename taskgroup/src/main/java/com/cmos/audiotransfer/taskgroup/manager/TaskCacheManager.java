package com.cmos.audiotransfer.taskgroup.manager;

import com.cmos.audiotransfer.common.utils.ConfigKey;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component public class TaskCacheManager {
    Map<String, String> defaultKeys = new HashMap<>();


    public String getDefaultKeys(String channelId) {
        if (defaultKeys.containsKey(channelId)) {
            return defaultKeys.get(channelId);
        } else
            return defaultKeys.put(channelId,
                new StringBuilder(ConfigKey.REDIS_QUEUE_PREFIX).append(channelId).toString());
    }


}
