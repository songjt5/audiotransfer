package com.cmos.audiotransfer.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final DateFormat dateParser = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final SimpleDateFormat jsonResultDateParser =
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String dateToString(Date date) {
        if (date == null)
            return null;
        return dateParser.format(date);
    }

    public static Date stringToDate(String timeString) {
        try {
            return dateParser.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long strToTimeStamp(String timeString) {
        Date date = stringToDate(timeString);
        return date == null ? null : date.getTime();
    }

    public static String dateToResultString(Date date) {
        if (date == null)
            return null;
        return jsonResultDateParser.format(date);
    }
}
