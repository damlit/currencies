package com.pocket.currencies;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication
public class CurrenciesApplication {

	// todo: 1.testy 2.circuit breaker 3.scheduler 4.cache
	public static void main(String[] args) {
		SpringApplication.run(CurrenciesApplication.class, args);
	}
}
