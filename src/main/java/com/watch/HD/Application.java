package com.watch.HD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
	@Bean
	public String getString(){return "";}
	@Bean
	public long getLong(){return 12;}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	}
