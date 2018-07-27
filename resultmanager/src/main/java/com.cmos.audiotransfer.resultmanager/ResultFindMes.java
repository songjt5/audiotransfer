package com.cmos.audiotransfer.resultmanager;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.resultmanager.Dao.ResultMongoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
public class ResultFindMes {
    @Autowired
    private ResultMongoDB resultMongoDB;

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String kkkk(){
        String channel = "371";
        String beginTime = "20180726145132";
        String transformBeginTime = "20180726145132";
        String id = "1";
        List<TaskBean> list = resultMongoDB.fingMesByTransBTime(channel,transformBeginTime);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stb = new StringBuilder("[");
        for(TaskBean t : list){
            stb.append("{"+ "\""+"channelId"+"\""+":"+"\""+t.getChannelId()+"\""+","+
                    "\""+"id"+"\""+":"+"\""+t.getId()+"," +
                    "\""+"beginTime"+"\""+":"+"\""+df.format(t.getBeginTime())+"," +
                    "\""+"path"+"\""+":"+"\""+t.getPath()+"," +
                    "\""+"status"+"\""+":"+"\""+t.getStatus()+"},"); //把你要拼接的字段放进去
        }
        String str = stb.substring(0, stb.length()-1);
        if(!"".equals(str)){
            str = str+"]";   //加上结尾，json就拼接完了
        }
        return str;
    }
}
