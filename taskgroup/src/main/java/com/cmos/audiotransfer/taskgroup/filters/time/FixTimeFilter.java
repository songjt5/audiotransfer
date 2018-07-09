package com.cmos.audiotransfer.taskgroup.filters.time;

import com.cmos.audiotransfer.common.utils.DateUtil;
import com.cmos.audiotransfer.taskgroup.utils.TaskPriority;

import java.util.Date;

public class FixTimeFilter extends TimeFilter {


    @Override protected TaskPriority getPriority(String value) {
        Date taskDate = DateUtil.stringToDate(value);
        if (taskDate == null)
            return TaskPriority.DEFAULT;
        if (fromTime == null ?
            true :
            taskDate.getTime() >= fromTime && toTime == null ? true : taskDate.getTime() <= toTime)
            return this.priortyValue;
        return TaskPriority.DEFAULT;
    }
}
