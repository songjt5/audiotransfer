package com.cmos.audiotransfer.resultmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.cmos.audiotransfer"})
@SpringBootApplication
public class ResultMangerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResultMangerApplication.class, args);

    }
}
