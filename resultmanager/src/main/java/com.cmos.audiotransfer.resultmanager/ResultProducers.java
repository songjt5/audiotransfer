package com.cmos.audiotransfer.resultmanager;

import com.cmos.audiotransfer.resultmanager.handlers.SendMessageProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResultProducers {
    @Autowired
    private SendMessageProducer producer;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String addMessage(@RequestParam(name = "msg") String msg) {

        Message msge = new Message("AAAASSSS", "asd", new String(
                "{\n" + "\t\"mesId\": \"1374\",\n" + "\t\"id\": \"2\",\n"
                        + "\t\"channelId\": \"374\",\n" + "\t\"status\": \"2\",\n"
                        + "\t\"path\": \"2\",\n" + "\t\"beginTime\":\"20180726145132\"\n"
                        + "}").getBytes());
        this.producer.send(msge);
        return "success";
    }

}
