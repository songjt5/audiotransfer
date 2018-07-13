package com.cmos.audiotransfer.taskgroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@ComponentScan({"com.cmos.audiotransfer"})
@SpringBootApplication public class TaskGroupApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskGroupApplication.class, args);

    }
    
}
