package com.lc.overseas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.lc.overseas.dao")
@ComponentScan(basePackages = {"com.lc.overseas.*"})
public class OverseasApplication {

    public static void main(String[] args) {
        SpringApplication.run(OverseasApplication.class, args);
    }

}
