package com.pocket.currencies.currencies;

import com.pocket.currencies.currencies.entity.Currency;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;

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
    @ResponseStatus(HttpStatus.OK)
    public String updateCurrencies() {
        if (currencyService.updateCurrencies()) {
            return "Quotes updated!";
        }
        return "Quotes update failed!";
    }
}
