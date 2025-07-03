package com.sideproject.spj001.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sideproject.spj001.interceptor.memLoginInterceptor;

@Configuration
public class webMvcConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new memLoginInterceptor())
				.addPathPatterns("/frontend/**")
				.excludePathPatterns(
						"/frontend/mem/memLoginPage",
						"/frontend/mem/loginMem",
						"/frontend/mem/logout",
						"/fronetend/mem/register",
						"/frontend/shop/**",
						"/static/**"
						);
	}
}
