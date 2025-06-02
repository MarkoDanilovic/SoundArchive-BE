package com.example.soundarchive.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("USERS-SERVICE", r -> r.path("/api/soundArchive/user/**", "/api/soundArchive/auth/**")
                        .uri("lb://USERS-SERVICE")) // Service name registered in Eureka
                .route("TRACK-SERVICE", r -> r.path("/api/soundArchive/track/**",
                                "/api/soundArchive/artist/**", "/api/soundArchive/record/**",
                                "/api/soundArchive/genre/**", "/api/soundArchive/medium/**",
                                "/api/soundArchive/wishlist/**", "/api/soundArchive/upload/**")
                        .uri("lb://TRACK-SERVICE")) // Service name registered in Eureka
                .route("UPLOADS", r -> r.path("/uploads/**")
                        .uri("lb://TRACK-SERVICE"))
                .route("ORDER-SERVICE", r -> r.path("/api/soundArchive/cart/**")
                        .uri("lb://ORDER-SERVICE")) // Service name registered in Eureka
                .build();
    }

}
