package com.cmos.audiotransfer.taskgroup.filters.string;

import com.cmos.audiotransfer.taskgroup.filters.Filter;
import com.cmos.audiotransfer.common.constant.TaskPriority;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFilter extends Filter {

    private Pattern p;

    public PatternFilter(String regex) {

        this.p = Pattern.compile(regex);
    }

    @Override protected TaskPriority getPriority(String value) {

        Matcher m = p.matcher(value.trim());
        if (m.matches())
            return this.priortyValue;
        return TaskPriority.DEFAULT;
    }
}
