package com.pocket.currencies.currencies;

import com.pocket.currencies.currencies.entity.Currency;

public interface CurrencyService {

    boolean updateCurrencies();
    String getLastQuotes(Currency targetCurrency);
    String getQuotes(Currency targetCurrency);
}
