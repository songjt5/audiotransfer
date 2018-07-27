package com.cmos.audiotransfer.transfermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication public class TransferManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransferManagerApplication.class, args);
    }

}
