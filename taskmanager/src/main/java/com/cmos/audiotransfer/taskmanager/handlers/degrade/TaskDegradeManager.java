package com.cmos.audiotransfer.taskmanager.handlers.degrade;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.taskmanager.constant.DispachConfigConsts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-14
 * Time: 下午1:40
 * Description:
 */
public class TaskDegradeManager {
    private Map<String, DegradeFilter> degradeFilters = new HashMap<>();

    Logger logger = LoggerFactory.getLogger(TaskDegradeManager.class);

    public void init(DegradeFilterConfig filterConfig) {

        for (Map<String, String> config : filterConfig.getFilterList()) {

            String channelId = config.get(ConfigConsts.TASK_CHANNELID);
            String timeUnit = config.get(DispachConfigConsts.DISPACH_DEGRADE_FILTER_TIMEUNIT);
            if (StringUtils.isEmpty(channelId) || StringUtils.isEmpty(timeUnit)) {
                logger.error("filter config is illegal:" + config.toString());
            }

            degradeFilters.put(channelId, new DegradeFilter(timeUnit
                .equalsIgnoreCase(DispachConfigConsts.DISPACH_DEGRADE_FILTER_TIMEUNIT_HOUR) ?
                Calendar.HOUR_OF_DAY :
                Calendar.DAY_OF_YEAR));
        }
    }

    public boolean checkDegrade(TaskBean task) {
        if (degradeFilters.containsKey(task.getChannelId())) {
            return degradeFilters.get(task.getChannelId()).apply(task);
        } else {
            return false;
        }

    }
}
