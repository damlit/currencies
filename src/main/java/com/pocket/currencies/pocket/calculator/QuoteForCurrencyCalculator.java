package com.pocket.currencies.pocket.calculator;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class QuoteForCurrencyCalculator {

    public BigDecimal getQuoteValueForCurrency(ExchangeQuote newestExchangeQuote, Currency currency) {
        return getQuoteForCurrency(newestExchangeQuote, currency)
                .map(Quote::getQuote)
                .orElse(BigDecimal.ONE);
    }

    public Optional<Quote> getQuoteForCurrency(ExchangeQuote newestExchangeQuote, Currency currency) {
        return newestExchangeQuote.getQuotes().stream()
                .filter(quote -> quote.getCurrency() == currency)
                .findFirst();
    }
}
