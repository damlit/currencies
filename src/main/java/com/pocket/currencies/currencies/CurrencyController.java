package com.pocket.currencies.currencies;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/last")
    public ResponseEntity<String> getLastCurrencies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("TestUserName: " + authentication.getName());
        return new ResponseEntity<>(currencyService.getLastCurrencies(), HttpStatus.OK);
    }

    @GetMapping(value = "/update")
    public ResponseEntity<String> updateCurrencies() {
        if (currencyService.updateCurrencies()) {
            return new ResponseEntity<>( "Quotes updated!", HttpStatus.OK);
        }
        return new ResponseEntity<>( "Quotes update failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
