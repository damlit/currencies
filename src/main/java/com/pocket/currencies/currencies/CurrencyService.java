package com.pocket.currencies.currencies;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;

import java.util.Date;

public interface CurrencyService {

    boolean updateCurrencies();
    String getLastQuotes(Currency targetCurrency);
    String getQuotes(Currency targetCurrency);
    ExchangeQuote getQuoteByDate(Date date, Currency targetCurrency);
}
