package com.sideproject.spj001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

//@EntityScan(basePackages = "com.sideproject.spj001.entity")
@SpringBootApplication
public class Spj001Application {

	public static void main(String[] args) {
		SpringApplication.run(Spj001Application.class, args);
		
		
		

	}
	
	
}