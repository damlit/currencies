package com.pocket.currencies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrenciesApplication {

	// todo: 1.REFACTOR wszystkiego(ENDPOINTY) 2.walidacja emaila 3.testy
	//  todo: 4.logika do liczenia(dodawanie/usuwanie wplat do portfela i liczenie po kursach)
	//  todo: 5.podzial na biznesowe pakiety 6.LOGer 7.Jakies odczytywanie usera z tokena (wyniesc do osobnej)
	//  todo: 8. exceptiony i poprawne statusy! 9.circuit breaker 10.swagger
	//  LAST.dodac MVP do GH
	public static void main(String[] args) {
		SpringApplication.run(CurrenciesApplication.class, args);
	}
}
