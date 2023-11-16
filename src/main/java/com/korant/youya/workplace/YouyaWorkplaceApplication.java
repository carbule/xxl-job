package com.korant.youya.workplace;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.korant.youya.workplace.mapper")
public class YouyaWorkplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouyaWorkplaceApplication.class, args);
    }

}
