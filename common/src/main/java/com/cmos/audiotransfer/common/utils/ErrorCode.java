package com.cmos.audiotransfer.common.utils;

public class ErrorCode {

    //任务状态:分组成功101,分发成功102,转写成功103,分组失败301,分发失败302,转写失败303
    public static final String TASK_STATUS_GROUPED = "101";
    public static final String TASK_STATUS_SEND = "102";
    public static final String TASK_STATUS_TRANSFERED = "103";
    public static final String TASK_STATUS_GROUPFAILED = "301";
    public static final String TASK_STATUS_MSGPARSEERROR = "30101";//消息解析失败
    public static final String TASK_STATUS_MISSKEY = "30102"; //缺少关键字段
    public static final String TASK_STATUS_SENDFAILED = "302";
    public static final String TASK_STATUS_TRANSFERFAILED = "303";
}
