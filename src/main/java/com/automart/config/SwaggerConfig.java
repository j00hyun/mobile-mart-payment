package com.automart.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static springfox.documentation.builders.RequestHandlerSelectors.withMethodAnnotation;

@Configuration
@EnableSwagger2 // Swagger2 버전을 활성화
public class SwaggerConfig implements WebMvcConfigurer {
    private String version;
    private String title;


    @Bean
    public Docket restApi() {
        version = "Demo Version";
        title = "automart API " + version;
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false) // 불필요한 응답코드(200, 401, 403, 404)와 메시지를 제거
                .select()// ApiSelectorBuilder를 생성
                .apis(RequestHandlerSelectors.any()) // api 스펙이 작성되어 있는 패키지를 지정
                .paths(PathSelectors.any()) // apis()로 선택되어진 API중 특정 path 조건에 맞는 API들을 다시 필터링하여 문서화
                .build()
                .apiInfo(apiInfo(title, version)) // 제목, 설명 등 문서에 대한 정보들을 보여주기 위해 호출
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .securitySchemes(Arrays.asList(apiKey()))
                .select(). // ApiSelectorBuilder 를 생성
                        apis(withMethodAnnotation(ApiOperation.class))
                .build();

    }

    /**
     * 제목, 설명 등 문서에 대한 정보
     */
    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .description("마트 자동결제화 애플리케이션 API Docs \n " +
                        "<a href=\"https://github.com/j00hyun/mobile-mart-payment\">" +
                        "저장소 사이트 이동하기</a>")
                .version(version)
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("jwtToken", "Authorization", "header");
    }

}
