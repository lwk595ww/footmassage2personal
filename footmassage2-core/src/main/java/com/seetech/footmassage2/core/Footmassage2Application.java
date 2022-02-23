package com.seetech.footmassage2.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@EnableDiscoveryClient //开启服务注册与发现
@SpringBootApplication
public class Footmassage2Application {
    public static void main(String[] args) {
        SpringApplication.run(Footmassage2Application.class, args);
    }
}
