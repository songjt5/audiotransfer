package com.cmos.audiotransfer.taskgroup.utils;

import com.cmos.audiotransfer.taskgroup.exception.Task;
import org.junit.Test;

import java.util.Map;

public class JSONUtilTest {
    @Test public void testToJson() {
        Task task = new Task();
        task.setId("sdfdsfds");
        task.setContactRecordId("111");
        task.setChannelId("sdnoif");
        task.setPath("/ss/wqe/sadsa/asd");
        System.out.println(JSONUtil.toJSON(task));
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
