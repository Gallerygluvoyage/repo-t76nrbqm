package com.campus.jobs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.campus.jobs.mapper")
public class CampusJobsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusJobsApplication.class, args);
    }
}
