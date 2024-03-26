package com.korant.youya.workplace;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(value = "com.korant.youya.workplace.mapper")
@EnableAsync
@EnableScheduling
public class YouyaWorkplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouyaWorkplaceApplication.class, args);
    }

}
