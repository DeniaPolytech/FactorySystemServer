package com.deniapolytech.FactorySystemWeb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(
                                "http://localhost:*",
                                "http://127.0.0.1:*",
                                "http://192.168.*:*", // Все порты для сети 192.168.x.x
                                "http://10.*.*.*:*",  // Если используете сеть 10.x.x.x
                                "http://172.16.*.*:*", // Если используете сеть 172.16.x.x
                                "http://[::1]:*" // IPv6
                        )
                        .allowedMethods("*") // Разрешить все методы
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization") // Если нужно
                        .allowCredentials(true)
                        .maxAge(3600); // Кэшировать предварительные запросы на 1 час
            }
        };
    }
}