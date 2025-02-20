package com.regain.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, LoggingGatewayFilterFactory loggingGatewayFilterFactory) {
        return builder.routes()
                .route(
                        "account-route", r -> r.path("/users/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(loggingGatewayFilterFactory.apply(new LoggingGatewayFilterFactory.Config()))
                                        .circuitBreaker(c -> c.setName("CircuitBreaker").getFallbackUri()))
                                .uri("lb://account-service"))
                .route(
                        "notification-route", r -> r.path("/notifications/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://notification-service"))
                .build();
    }
}
