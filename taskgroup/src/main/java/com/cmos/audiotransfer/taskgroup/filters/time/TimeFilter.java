package com.cmos.audiotransfer.taskgroup.filters.time;

import com.cmos.audiotransfer.taskgroup.filters.Filter;

public abstract class TimeFilter extends Filter {
    /*是否为动态区间*/
    protected Boolean isDynamic;
    /*开始时间的毫秒数表示,当isDynamic为true时代表开始时间距离当前时间的毫秒数*/
    protected Long fromTime;
    /*结束时间的毫秒数表示,当isDynamic为true时代表结束时间距离当前时间的毫秒数*/
    protected Long toTime;

    public Boolean getDynamic() {
        return isDynamic;
    }

    public void setDynamic(Boolean dynamic) {
        isDynamic = dynamic;
    }

    public Long getFromTime() {
        return fromTime;
    }

    public void setFromTime(Long fromTime) {
        this.fromTime = fromTime;
    }

    public Long getToTime() {
        return toTime;
    }

    public void setToTime(Long toTime) {
        this.toTime = toTime;
    }
}
