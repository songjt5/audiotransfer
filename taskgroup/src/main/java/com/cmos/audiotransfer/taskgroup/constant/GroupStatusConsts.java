package com.cmos.audiotransfer.taskgroup.constant;

public class GroupStatusConsts {

    //任务状态:分组成功101,分组失败301
    public static final String TASK_STATUS_GROUPED = "100";
    public static final String TASK_STATUS_GROUPFAILED = "101";
    public static final String TASK_STATUS_MSGPARSEERROR = "10101";//消息解析失败
    public static final String TASK_STATUS_MISSKEY = "10102"; //缺少关键字段
    public static final String TASK_STATUS_REDISCONECTFAILED = "10103";

}
