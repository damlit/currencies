package com.pocket.currencies.currencies;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/last")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExchangeQuote getLastCurrencies(@RequestParam("currency") String currency) {
        return currencyService.getLastQuotes(Currency.valueOf(currency));
    }

    @GetMapping(value = "/quotes")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ExchangeQuote> getQuotes(@RequestParam("currency") String currency) {
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

    @GetMapping(value = "/date")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExchangeQuote getQuoteByDate(@RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy") Date date,
                                        @RequestParam("currency") String currency) {
        return currencyService.getQuoteByDate(date, Currency.valueOf(currency));
    }
}
