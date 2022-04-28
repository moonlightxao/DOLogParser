package edu.dolp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MoLFIParser9002Main {
    public static void main(String[] args) {
        SpringApplication.run(MoLFIParser9002Main.class, args);
    }
}
