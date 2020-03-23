package com.sergeykotov.op;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OpApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpApplication.class, args);
    }
}