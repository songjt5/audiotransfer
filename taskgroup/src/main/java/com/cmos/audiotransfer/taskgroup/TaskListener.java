package com.cmos.audiotransfer.taskgroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController public class TaskListener {

    private final KafkaTemplate kafkaTemplate;

    @Autowired public TaskListener(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String addMessage(@RequestParam(name = "msg") String msg) {

        this.kafkaTemplate.send("consumer_task_origin",
            "{\n" + "\t\"channelId\": \"1\",\n" + "\t\"id\": \"asdsad\",\n"
                + "\t\"number\": \"60\",\n" + "\t\"path\": \"asdasdas\",\n"
                + "\t\"date\": \"20180405112233\"\n" + "}");
        return "success";
    }
    // ...

}
