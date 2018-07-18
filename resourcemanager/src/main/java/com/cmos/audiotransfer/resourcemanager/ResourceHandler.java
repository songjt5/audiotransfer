package com.cmos.audiotransfer.resourcemanager;

import com.cmos.audiotransfer.resourcemanager.manager.ResourceCapacityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hejie
 * Date: 18-7-11
 * Time: 上午10:15
 * Description:
 */
@RestController public class ResourceHandler {
    @Autowired ResourceCapacityManager config;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String addMessage() {


        return config.getCapacities().values().toString();
        /*Message msge = new Message(TopicConsts.TOPIC_TRANSFER_RESOURCE, TopicConsts.TOPIC_TRANSFER_RESOURCE, new String(
            "{\n" + "\t\"typeName\": \"河南\",\n" + "\t\"typeCode\": \"1\",\n"
                + "\t\"innerCode\": 1\n" + "}").getBytes());
        this.producer.send(msge);
        return "success";*/
    }
    // ...

}
