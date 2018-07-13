package com.cmos.audiotransfer.taskgroup.filters.string;

import com.cmos.audiotransfer.taskgroup.filters.Filter;
import com.cmos.audiotransfer.common.constant.TaskPriority;

import java.util.Set;

public class ContainFilter extends Filter {

    private Set<String> values;


    @Override protected TaskPriority getPriority(String value) {
        if (values.contains(value.trim()))
            return this.priortyValue;
        return TaskPriority.DEFAULT;
    }


    public ContainFilter(Set<String> values) {

        this.values = values;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }
}
