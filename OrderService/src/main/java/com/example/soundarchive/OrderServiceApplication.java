package com.example.soundarchive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
public class OrderServiceApplication
{

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
