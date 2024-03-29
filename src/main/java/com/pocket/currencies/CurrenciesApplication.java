package com.pocket.currencies;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableEncryptableProperties
@EnableRetry
@SpringBootApplication
public class CurrenciesApplication {

	// todo: 1.scheduler 2.cache 3.why USD disappeared 4.auto update if quotes doesn't exist 5.403 wo. register
	// encrypt key -Djasypt.encryptor.password=SECRET_KEY -e DB_ENDPOINT=localhost
	public static void main(String[] args) {
		SpringApplication.run(CurrenciesApplication.class, args);
	}
}
