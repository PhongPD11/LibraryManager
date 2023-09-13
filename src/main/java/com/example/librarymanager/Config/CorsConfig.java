package com.example.librarymanager.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:5500"); // Cho phép tất cả các origin
        corsConfiguration.addAllowedOrigin("http://192.168.1.19:5500"); // Cho phép tất cả các origin
        corsConfiguration.setAllowCredentials(true); // Cho phép credentials mode
        corsConfiguration.addAllowedMethod("*"); // Cho phép tất cả các phương thức HTTP
        corsConfiguration.addAllowedHeader("*"); // Cho phép tất cả các header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}