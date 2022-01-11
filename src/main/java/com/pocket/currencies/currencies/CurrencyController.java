package com.pocket.currencies.currencies;

import com.pocket.currencies.currencies.entity.Currency;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/last")
    @ResponseStatus(HttpStatus.OK)
    public String getLastCurrencies(@RequestParam("currency") String currency) {
        return currencyService.getLastQuotes(Currency.valueOf(currency));
    }

    @GetMapping(value = "/quotes")
    @ResponseStatus(HttpStatus.OK)
    public String getQuotes(@RequestParam("currency") String currency) {
        return currencyService.getQuotes(Currency.valueOf(currency));
    }

    @GetMapping(value = "/update")
    public ResponseEntity<String> updateCurrencies() {
        if (currencyService.updateCurrencies()) {
            return new ResponseEntity<>( "Quotes updated!", HttpStatus.OK);
        }
        return new ResponseEntity<>( "Quotes update failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
