package edu.dolp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("edu.dolp.mapper")
public class LogDatabaseMain {
    public static void main(String[] args) {
        SpringApplication.run(LogDatabaseMain.class, args);
    }
}
