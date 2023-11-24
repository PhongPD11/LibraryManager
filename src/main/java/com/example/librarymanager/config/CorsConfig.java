package com.example.librarymanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://127.0.0.1:5500"); // Cho phép tất cả các origin
        config.addAllowedOrigin("http://192.168.1.1:5500");
        config.addAllowedOrigin("http://192.168.1.2:5500");
        config.addAllowedOrigin("http://192.168.1.3:5500");
        config.addAllowedOrigin("http://192.168.1.4:5500");
        config.addAllowedOrigin("http://192.168.1.5:5500");
        config.addAllowedOrigin("http://192.168.1.6:5500");
        config.addAllowedOrigin("http://192.168.1.7:5500");
        config.addAllowedOrigin("http://192.168.1.8:5500");
        config.addAllowedOrigin("http://192.168.1.9:5500");
        config.addAllowedOrigin("http://192.168.1.10:5500");
        config.addAllowedOrigin("http://192.168.1.11:5500");
        config.addAllowedOrigin("http://192.168.1.12:5500");
        config.addAllowedOrigin("http://192.168.1.13:5500");
        config.addAllowedOrigin("http://192.168.1.14:5500");
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
