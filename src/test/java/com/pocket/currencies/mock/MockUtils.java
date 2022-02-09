package com.pocket.currencies.mock;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Pocket;
import com.pocket.currencies.pocket.entity.Profit;
import com.pocket.currencies.users.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MockUtils {

    public static User getMockedUser() {
        return User.builder()
                .email("test@test.pl")
                .build();
    }

    public static Deposit createMockDeposit(Currency boughtCurrency, Currency soldCurrency, BigDecimal quote) {
        return Deposit.builder()
                .id(100)
                .soldCurrency(soldCurrency)
                .boughtCurrency(boughtCurrency)
                .quote(quote)
                .pocket(null)
                .soldSum(BigDecimal.TEN)
                .boughtSum(BigDecimal.TEN.divide(quote, RoundingMode.HALF_DOWN))
                .build();
    }

    public static Deposit createMockDeposit(Currency boughtCurrency, BigDecimal quote, Pocket pocket) {
        return Deposit.builder()
                .soldCurrency(Currency.PLN)
                .boughtCurrency(boughtCurrency)
                .quote(quote)
                .pocket(pocket)
                .soldSum(BigDecimal.TEN)
                .boughtSum(BigDecimal.TEN.divide(quote, RoundingMode.HALF_DOWN))
                .build();
    }

    public static Profit createMockProfit(Currency boughtCurrency, Currency soldCurrency, double profit) {
        return Profit.builder()
                .depositId((int) profit)
                .profit(BigDecimal.valueOf(profit))
                .soldCurrency(soldCurrency)
                .boughtCurrency(boughtCurrency)
                .soldSum(BigDecimal.TEN)
                .build();
    }
}
