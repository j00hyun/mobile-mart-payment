package com.automart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
public class AutomartApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "/home/ubuntu/devops/application.yml,"
			+ "/home/ubuntu/devops/application-auth.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(AutomartApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}

}
