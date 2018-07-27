package com.cmos.audiotransfer.resultmanager.Dao;

import com.alibaba.fastjson.JSONObject;
import com.cmos.audiotransfer.common.bean.TaskBean;

import java.util.Date;
import java.util.List;

public interface ResultMongoDB {
    //添加一条新纪录
    void SaveMes(TaskBean taskBean);

    //更新记录信息
    void UpdaMes(TaskBean taskBean);

    //通过渠道号和接触时间查询
    List<TaskBean> fingMesByChanTime(String channel, String beginTime);

    //通过渠道号和流水号查询
    List<TaskBean> fingMesByChanId(String channel, String id);

    //通过渠道号和转写时间查询
    List<TaskBean> fingMesByTransBTime(String channel, String transformBeginTime);
}
