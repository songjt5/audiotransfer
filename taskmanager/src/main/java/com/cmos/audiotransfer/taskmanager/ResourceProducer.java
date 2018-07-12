package com.cmos.audiotransfer.taskmanager;

import com.cmos.audiotransfer.common.utils.ConfigKey;
import com.cmos.audiotransfer.taskmanager.handlers.SendMessageProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hejie
 * Date: 18-7-11
 * Time: 上午10:15
 * Description:
 */
@RestController public class ResourceProducer {
    @Autowired SendMessageProducer producer;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String addMessage(@RequestParam(name = "msg") String msg) {

        Message msge = new Message(ConfigKey.RESOURCE_TOPIC, ConfigKey.RESOURCE_TOPIC, new String(
            "{\n" + "\t\"typeName\": \"河南\",\n" + "\t\"typeCode\": \"1\",\n"
                + "\t\"innerCode\": 1\n" + "}").getBytes());
        this.producer.send(msge);
        return "success";
    }
    // ...

}
