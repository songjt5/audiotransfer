package com.cmos.audiotransfer.resultmanager.DaoImpl;

import com.cmos.audiotransfer.common.bean.TaskBean;
import com.cmos.audiotransfer.resultmanager.Dao.ResultMongoDB;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

@Component
public class ResultMongoDBImpl implements ResultMongoDB {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void SaveMes(TaskBean taskBean) {
        try {
            mongoTemplate.save(taskBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UpdaMes(TaskBean taskBean) {
        Query query = new Query();
        query.addCriteria(Criteria.where("mes_id").is("1373"));
        Document taskInfo = new Document();
        Class clazz = taskBean.getClass();
        Field[] fields = clazz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    if(null != field.get(taskBean) && !(" ").equals(field.get(taskBean))){
                        taskInfo.put(field.getName(), field.get(taskBean));
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Update update = Update.fromDocument(taskInfo);
        try{
            mongoTemplate.upsert(query, update, "task_status");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<TaskBean> fingMesByChanTime(String channel, String beginTime) {
        Query query = new Query();
        Date bTime = strToDateLong(beginTime);
        query.addCriteria(Criteria.where("channel").is(channel).and("start_t").is(bTime));
        List<TaskBean> list = mongoTemplate.find(query, TaskBean.class, "task_status");
        return list;
    }

    @Override
    public List<TaskBean> fingMesByChanId(String channel, String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("channel").is(channel).and("id").is(id));
        List<TaskBean> list = mongoTemplate.find(query, TaskBean.class, "task_status");
        return list;
    }

    @Override
    public List<TaskBean> fingMesByTransBTime(String channel, String transformBeginTime) {
        Query query = new Query();
        Date transBTime = strToDateLong(transformBeginTime);
        query.addCriteria(Criteria.where("channel").is(channel).and("trans_b_t").is(transBTime));
        List<TaskBean> list = mongoTemplate.find(query, TaskBean.class, "task_status");
        return list;
    }

    /**
         * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
         *这里将传入的String类型的beginTime转换为Date类型传入数据库进行比较
         * @param
         * @return
         */
    public static Date strToDateLong(String strDate) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    ParsePosition pos = new ParsePosition(0);
    Date strtodate = formatter.parse(strDate, pos);
    return strtodate;
     }

}
