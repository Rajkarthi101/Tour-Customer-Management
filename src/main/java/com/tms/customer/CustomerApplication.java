package com.tms.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.tms.customer.filters.JWTFilter;

//spring boot application class
@SpringBootApplication
@EnableFeignClients
public class CustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	// Add jwt filter to filters list and ensure it acts only on certain paths
	@Bean
	public FilterRegistrationBean<JWTFilter> jwtFilter() {
		FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JWTFilter());
		// Only apply filter to non post customer paths
		registrationBean.addUrlPatterns("/auth/*");

		return registrationBean;
	}

}
