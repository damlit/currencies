package com.pocket.currencies.currencies.entity;

import lombok.Getter;

@Getter
public enum Currency {
    PLN("PLN"),
    EUR("EUR"),
    USD("USD"),
    GBP("GBP");

    private final String currency;

    private Currency(String currency) {
        this.currency = currency;
    }

    public static boolean containsCurrency(String currencyToCheck) {
        try {
            Currency.valueOf(currencyToCheck);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
