package com.codenotfound.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringWsServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringWsServerApplication.class, args);
  }
}
