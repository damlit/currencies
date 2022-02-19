package com.pocket.currencies.currencies;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;

import java.util.Date;
import java.util.List;

public interface CurrencyService {

    boolean updateCurrencies();
    ExchangeQuote getLastQuotes(Currency targetCurrency);
    List<ExchangeQuote> getQuotes(Currency targetCurrency);
    ExchangeQuote getQuoteByDate(Date date, Currency targetCurrency);
}
