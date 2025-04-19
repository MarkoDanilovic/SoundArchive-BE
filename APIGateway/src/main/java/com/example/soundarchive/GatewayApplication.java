package com.example.soundarchive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * Hello world!
 *
 */
@SpringBootApplication

public class GatewayApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
