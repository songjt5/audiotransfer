package com.cmos.audiotransfer.taskmanager.handlers.degrade;

import com.cmos.audiotransfer.common.bean.TaskBean;

import java.util.Calendar;

/**
 * Created by hejie
 * Date: 18-7-14
 * Time: 下午1:46
 * Description:
 */
public class DegradeFilter {

    Integer timeUnit;

    public DegradeFilter(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean apply(TaskBean task) {
        Calendar groupTime = Calendar.getInstance();
        int now = groupTime.get(timeUnit);
        groupTime.setTime(task.getGroupTime());
        int taskTime = groupTime.get(timeUnit);
        if (now == taskTime) {
            return false;
        } else {
            return true;
        }
    }
}
