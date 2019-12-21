package com.example.loggenerate;

import com.example.loggenerate.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class LoggenerateApplication {
    public static final AtomicInteger atomicInteger = new AtomicInteger();
    public static final DateTimeFormatter YMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter SEQFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final Logger logger = LoggerFactory
            .getLogger(LoggenerateApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(LoggenerateApplication.class, args);
    }

}
