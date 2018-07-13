package com.cmos.audiotransfer.taskgroup.manager;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
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
                new StringBuilder(ConfigConsts.TASK_QUEUE_KEY_PREFIX).append(channelId).toString());
    }


}
