package com.cmos.audiotransfer.taskgroup.filters.number;

import com.cmos.audiotransfer.taskgroup.util.TaskPriority;

public class EqualFilter extends NumberFilter {

    Double value;
    @Override protected TaskPriority getPriority(String value) {
        if (Double.parseDouble(value) == this.value)
            return this.priortyValue;
        else
            return TaskPriority.DEFAULT;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
