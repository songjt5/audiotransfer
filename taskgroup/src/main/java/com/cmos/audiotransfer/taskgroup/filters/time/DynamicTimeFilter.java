package com.cmos.audiotransfer.taskgroup.filters.time;

import com.cmos.audiotransfer.common.util.DateUtil;
import com.cmos.audiotransfer.taskgroup.util.TaskPriority;

import java.util.Date;

public class DynamicTimeFilter extends TimeFilter {


    @Override protected TaskPriority getPriority(String value) {
        Date taskDate = DateUtil.stringToDate(value);
        if (taskDate == null)
            return TaskPriority.DEFAULT;
        long current = new Date().getTime();
        if (fromTime == null ?
            true :
            taskDate.getTime() >= current - fromTime && toTime == null ?
                taskDate.getTime() <= current :
                taskDate.getTime() <= current - toTime) {
            return this.priortyValue;
        } else
            return TaskPriority.DEFAULT;
    }

}
