package com.buddy;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 */
@SpringBootApplication
@MapperScan("com.buddy.mapper")
@EnableScheduling
public class MyApplication {

    private static Logger logger = LoggerFactory.getLogger(MyApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
        logger.info("服务启动成功！");
    }

}
