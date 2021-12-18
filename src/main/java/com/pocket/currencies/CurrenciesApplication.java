package com.pocket.currencies;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication
public class CurrenciesApplication {

	// todo: 1.circuit breaker 2.scheduler 3.cache
	// encrypt key -Djasypt.encryptor.password=SECRET_KEY
	public static void main(String[] args) {
		SpringApplication.run(CurrenciesApplication.class, args);
	}
}
