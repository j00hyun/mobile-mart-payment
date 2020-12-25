package com.automart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // CORS를 적용할 URL 패턴 /**은 와일드 카드를 의미
                .allowedOrigins("*")       // 자원을 공유를 허락할 Origin을 지정
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 요청 허용 메소드
                .allowedHeaders("*") // 요청 허용하는 헤더
                .allowCredentials(true) // 쿠키 허용
                .maxAge(MAX_AGE_SECS);
    }
}
