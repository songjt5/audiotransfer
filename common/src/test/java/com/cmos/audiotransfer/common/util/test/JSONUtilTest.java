package com.cmos.audiotransfer.common.util.test;

import com.cmos.audiotransfer.common.bean.TransformResource;
import com.cmos.audiotransfer.common.exception.Task;
import com.cmos.audiotransfer.common.util.JSONUtil;
import org.junit.Test;

import java.util.Map;

public class JSONUtilTest {
    @Test public void testToJson() {
        /*Task task = new Task();
        task.setId("sdfdsfds");
        task.setContactRecordId("111");
        task.setChannelId("sdnoif");
        task.setPath("/ss/wqe/sadsa/asd");
        System.out.println(JSONUtil.toJSON(task));*/
        TransformResource resource = new TransformResource();
        resource.setTypeName("河南");
        resource.setInnerCode(1);
        resource.setTypeCode("1");
        System.out.println(JSONUtil.toJSON(resource).toString());
    }

    @Test public void testFromJson() {
        Task task = new Task();
        task.setId("sdfdsfds");
        task.setContactRecordId("111");
        task.setChannelId("sdnoif");
        task.setPath("/ss/wqe/sadsa/asd");
        Task task2 = JSONUtil.fromJson(JSONUtil.toJSON(task), Task.class);
        System.out.println(task2);

    }

    @Test public void testToMap() {
        Task task = new Task();
        task.setId("sdfdsfds");
        task.setContactRecordId("111");
        task.setChannelId("sdnoif");
        task.setPath("/ss/wqe/sadsa/asd");
        Map<String,String> task2 = JSONUtil.toMap(JSONUtil.toJSON(task));
        System.out.println(task2);

    }
}
