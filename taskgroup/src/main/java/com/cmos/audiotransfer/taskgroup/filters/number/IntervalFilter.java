package com.cmos.audiotransfer.taskgroup.filters.number;

import com.cmos.audiotransfer.taskgroup.utils.TaskPriority;

public class IntervalFilter extends NumberFilter {

    private Double fromNum;

    private Double toNum;
    /*区间类型:0为全开区间,1为左开右闭区间,2为左闭右开区间，3为全闭区间*/
    private int intervalType;

    public IntervalFilter() {

    }
    public IntervalFilter(int intervalType) {
        this.intervalType = intervalType;
    }

    @Override protected TaskPriority getPriority(String value) {
        try {
            Double numValue = Double.parseDouble(value);
            if (intervalType > 3)
                return TaskPriority.DEFAULT;
            if (fromNum == null ?
                true :
                ((intervalType & 2) == 2 ? numValue >= fromNum : numValue > fromNum)
                    && toNum == null ?
                    true :
                    ((intervalType & 1) == 1 ? numValue <= toNum : numValue < toNum)) {
                return this.priortyValue;
            } else
                return TaskPriority.DEFAULT;
        } catch (Exception e) {
            e.printStackTrace();
            return TaskPriority.DEFAULT;
        }

    }

    public Double getFromNum() {
        return fromNum;
    }

    public void setFromNum(Double fromNum) {
        this.fromNum = fromNum;
    }

    public Double getToNum() {
        return toNum;
    }

    public void setToNum(Double toNum) {
        this.toNum = toNum;
    }

    public Integer getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(Integer intervalType) {
        this.intervalType = intervalType;
    }

}
