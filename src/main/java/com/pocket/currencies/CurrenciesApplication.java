package com.pocket.currencies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrenciesApplication {

	// todo: 1.testy 2.logi 3. circuit breaker 4.scheduler 5.cache
	public static void main(String[] args) {
		SpringApplication.run(CurrenciesApplication.class, args);
	}
}
