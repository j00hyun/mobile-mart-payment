package com.automart;

import com.automart.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AutomartApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomartApplication.class, args);
	}

}
