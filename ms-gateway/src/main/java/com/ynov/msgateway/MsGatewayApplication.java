package com.ynov.msgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsGatewayApplication.class, args);
  }

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    return builder.routes()
                  .route("client-service", r -> r.path("/clients/**").uri("lb://MS-CLIENT"))
                  .route("account-service", r -> r.path("/accounts/**").uri("lb://MS-ACCOUNT"))
                  .route("operation-service", r -> r.path("/operations/**").uri("lb://MS-OPERATION"))
                  .build();
  }


}
