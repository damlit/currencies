package com.pocket.currencies.currencies;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/last")
    @ResponseStatus(HttpStatus.OK)
    public String getLastCurrencies() {
        return currencyService.getLastQuotes();
    }

    @GetMapping(value = "/quotes")
    @ResponseStatus(HttpStatus.OK)
    public String getQuotes() {
        return currencyService.getQuotes();
    }

    @GetMapping(value = "/update")
    public ResponseEntity<String> updateCurrencies() {
        if (currencyService.updateCurrencies()) {
            return new ResponseEntity<>( "Quotes updated!", HttpStatus.OK);
        }
        return new ResponseEntity<>( "Quotes update failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
