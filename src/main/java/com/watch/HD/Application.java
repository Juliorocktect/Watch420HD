package com.watch.HD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class Application {
	@Bean
	public String getString(){return "";}
	@Bean
	public long getLong(){return 12;}
	@Bean
	public int getInt(){return 22;}
	@Bean
	public LocalDateTime getTime(){return LocalDateTime.now();}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	}
