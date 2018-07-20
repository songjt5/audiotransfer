package com.cmos.audiotransfer.taskgroup.filters;

import com.cmos.audiotransfer.common.constant.ConfigConsts;
import com.cmos.audiotransfer.common.util.DateUtils;
import com.cmos.audiotransfer.taskgroup.constant.FilterConfigConsts;
import com.cmos.audiotransfer.taskgroup.filters.number.EqualFilter;
import com.cmos.audiotransfer.taskgroup.filters.number.IntervalFilter;
import com.cmos.audiotransfer.taskgroup.filters.number.NumberFilter;
import com.cmos.audiotransfer.taskgroup.filters.string.ContainFilter;
import com.cmos.audiotransfer.taskgroup.filters.string.PatternFilter;
import com.cmos.audiotransfer.taskgroup.filters.time.DynamicTimeFilter;
import com.cmos.audiotransfer.taskgroup.filters.time.FixTimeFilter;
import com.cmos.audiotransfer.taskgroup.filters.time.TimeFilter;
import com.cmos.audiotransfer.common.constant.TaskPriority;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component public class FilterFactory {

    private static final Logger logger = LoggerFactory.getLogger(FilterFactory.class);

    public Map<String, List<Filter>> getFilters(FilterFactory filterFactory,
        FilterConfigs filterConfig) {

        Map<String, List<Filter>> filters = filterConfig.getFilterList().stream().map(p -> {
            String type = p.get(FilterConfigConsts.FILTER_TYPE);
            type = type.trim();
            if (FilterConfigConsts.FILTER_TYPE_STRING.equalsIgnoreCase(type)) {
                return filterFactory.createStringFilter(p);
            } else if (FilterConfigConsts.FILTER_TYPE_TIME.equalsIgnoreCase(type)) {
                return filterFactory.createTimeFilter(p);
            } else if (FilterConfigConsts.FILTER_TYPE_NUMBER.equalsIgnoreCase(type)) {
                return filterFactory.createNumberFilter(p);
            } else
                return null;
        }).filter(p -> p != null).collect(Collectors.groupingBy(Filter::getChannel));
        filters.forEach((k, v) -> {
            v.stream().sorted((t0, t1) -> {
                if (Integer.parseInt(t0.getPriortyValue().getValue()) > Integer
                    .parseInt(t1.getPriortyValue().getValue()))
                    return -1;
                if (t0.getPriortyValue().getValue().equals(t1.getPriortyValue().getValue())) {
                    return 0;
                } else
                    return 1;

            }).collect(Collectors.toList());
        });
        return filters;
    }

    public Filter createStringFilter(Map<String, String> filterInfo) {
        Filter filter;
        String value = filterInfo.get(FilterConfigConsts.FILTER_VALUE);
        if (!StringUtils.isEmpty(value)) {
            Set<String> values = new HashSet<>();
            CollectionUtils.addAll(values, value.split(","));
            filter = new ContainFilter(values);
        } else {
            String patternStr = filterInfo.get(FilterConfigConsts.FILTER_STRING_PATTERN);
            if (StringUtils.isEmpty(patternStr)) {
                // TODO
                return null;
            }
            try {
                filter = new PatternFilter(patternStr);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (initFilter(filter, filterInfo))
            return filter;
        else
            return null;

    }

    public Filter createTimeFilter(Map<String, String> filterInfo) {

        String dynamicStr = filterInfo.get(FilterConfigConsts.FILTER_TIME_DYNAMICFLAG);
        if (StringUtils.isEmpty(dynamicStr))
            return null;
        TimeFilter filter = null;
        Long fromTime = null;
        Long toTime = null;

        if ("true".equalsIgnoreCase(dynamicStr.trim())) {
            filter = new DynamicTimeFilter();
            boolean flag = false;
            try {
                fromTime = parseTimeStr(filterInfo.get(FilterConfigConsts.FILTER_TIME_FROM));
                flag = true;
            } catch (NumberFormatException e) {
                //todo
                e.printStackTrace();
            }
            try {
                toTime = parseTimeStr(filterInfo.get(FilterConfigConsts.FILTER_TIME_FROM));
            } catch (NumberFormatException e) {
                if (flag = false) {
                    //todo
                    return null;
                }
            }
            Long current = new Date().getTime();
            if (fromTime != null)
                fromTime = current - fromTime;

            if (toTime != null)
                toTime = current - toTime;

        } else {
            filter = new FixTimeFilter();
            boolean flag = false;
            fromTime = parseDateStr(filterInfo.get(FilterConfigConsts.FILTER_TIME_FROM));
            toTime = parseDateStr(filterInfo.get(FilterConfigConsts.FILTER_TIME_TO));
            if (fromTime == null && toTime == null) {
                //todo
                return null;
            }

        }
        if (initFilter(filter, filterInfo) && isValidTime(fromTime, toTime)) {
            filter.setFromTime(fromTime);
            filter.setToTime(toTime);
            return filter;
        } else {
            return null;
        }
    }


    public Filter createNumberFilter(Map<String, String> filterInfo) {
        String intervalType = filterInfo.get(FilterConfigConsts.FILTER_NUMBER_INTERVALTYPE);
        NumberFilter filter = null;
        if (checkIntervalType(intervalType)) {
            Double from = Double.parseDouble(filterInfo.get(FilterConfigConsts.FILTER_NUMBER_FROM));
            Double to = Double.parseDouble(filterInfo.get(FilterConfigConsts.FILTER_NUMBER_TO));
            if (isValidNum(from, to)) {
                IntervalFilter intervalFilter = new IntervalFilter();
                intervalFilter.setIntervalType(Integer.parseInt(intervalType));
                intervalFilter.setFromNum(from);
                intervalFilter.setToNum(to);
                filter = intervalFilter;
            } else
                return null;
        } else {
            try {
                Double value = Double.parseDouble(filterInfo.get(FilterConfigConsts.FILTER_VALUE));
                EqualFilter equalFilter = new EqualFilter();
                equalFilter.setValue(value);
                filter = equalFilter;
            } catch (NumberFormatException e) {

                e.printStackTrace();
                return null;
            }
        }
        if (initFilter(filter, filterInfo))
            return filter;
        else
            return null;
    }

    private boolean initFilter(Filter filter, Map<String, String> filterInfo) {

        String id = filterInfo.get(ConfigConsts.FILTER_ID);
        if (StringUtils.isEmpty(id)) {
            return false;
        } else {
            filter.setId(id);
        }
        String channel = filterInfo.get(ConfigConsts.TASK_CHANNELID);
        if (StringUtils.isEmpty(channel))
            return false;
        filter.setChannel(channel);
        String key = filterInfo.get(FilterConfigConsts.FILTER_KEY);
        if (StringUtils.isEmpty(key))
            return false;
        filter.setKey(key);
        String priority = filterInfo.get(FilterConfigConsts.FILTER_TASK_PRIORITY_VALUE);
        if (StringUtils.isEmpty(priority) || !checkPriority(priority.trim()))
            return false;
        setPriority(filter, priority);
        return true;
    }

    private void setPriority(Filter filter, String priority) {
        switch (priority.trim()) {
            case ("3"):
                filter.setPriortyValue(TaskPriority.THREE);
                break;
            case ("2"):
                filter.setPriortyValue(TaskPriority.TWO);
                break;
            case ("1"):
                filter.setPriortyValue(TaskPriority.ONE);
                break;
            case ("0"):
                filter.setPriortyValue(TaskPriority.ZERO);
                break;
            default:
                filter.setPriortyValue(TaskPriority.DEFAULT);
                break;
        }
    }

    private boolean checkPriority(String str) {
        if (Pattern.matches("^[0-3]$", str))
            return true;
        else
            return false;
    }

    private boolean isValidTime(Long fromTime, Long toTime) {

        if (fromTime == null && toTime == null)
            return false;
        if (fromTime != null && fromTime < 0)
            return false;
        if (toTime != null && toTime < 0)
            return false;
        if (fromTime != null && toTime != null && fromTime >= toTime)
            return false;
        return true;
    }

    private boolean isValidNum(Double from, Double to) {

        if (from == null && to == null)
            return false;
        if (from != null && to != null && from >= to)
            return false;
        return true;
    }

    private boolean checkIntervalType(String str) {

        if (StringUtils.isEmpty(str))
            return false;
        if (Pattern.matches("^[0-3]$", str))
            return true;
        else
            return false;

    }


    private Long parseTimeStr(String str) {
        String[] params = str.split("-");
        if (params.length != 3) {
            //todo
            return null;
        }
        Long hours = null;
        Long minutes = null;
        Long seconds = null;
        hours = Long.parseLong(params[0]);
        minutes = Long.parseLong(params[1]);
        seconds = Long.parseLong(params[2]);

        if (hours != null && minutes != null && seconds != null && minutes >= 0 && minutes < 60
            && seconds >= 0 && seconds < 60) {

            return (hours * 3600 + minutes * 60 + seconds) * 1000;
        } else {
            //todo
            return null;
        }
    }

    private Long parseDateStr(String str) {
        return DateUtils.strToTimeStamp(str);
    }


    public static void main(String[] args) {

        System.out.println();

    }
}
