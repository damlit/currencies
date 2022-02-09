package com.pocket.currencies.pocket.calculator;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Profit;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@AllArgsConstructor
public class OneDepositProfitCalculator {

    private final Logger LOG = LoggerFactory.getLogger("logger");
    private final int SCALE = 6;

    private final QuoteForCurrencyCalculator quoteForCurrencyCalculator;

    public Profit calculateProfitFromOneDeposit(ExchangeQuote newestExchangeQuote, Deposit deposit, BigDecimal plnQuote) {
        BigDecimal profitForCurrency = calculateQuoteForCurrency(newestExchangeQuote, deposit, plnQuote);
        return buildProfit(deposit, profitForCurrency);
    }

    private BigDecimal calculateQuoteForCurrency(ExchangeQuote newestExchangeQuote, Deposit deposit, BigDecimal plnQuote) {
        return quoteForCurrencyCalculator.getQuoteForCurrency(newestExchangeQuote, getCurrencyOtherThanPln(deposit))
                .map(Quote::getQuote)
                .map(newestQuote -> calculateDifference(deposit, newestQuote, plnQuote))
                .orElse(BigDecimal.ZERO);
    }

    private Currency getCurrencyOtherThanPln(Deposit deposit) {
        if (deposit.getBoughtCurrency() == Currency.PLN) {
            return deposit.getSoldCurrency();
        }
        return deposit.getBoughtCurrency();
    }

    private BigDecimal calculateDifference(Deposit deposit, BigDecimal newestQuote, BigDecimal plnQuote) {
        LOG.info("Calculating difference for " + deposit.getBoughtCurrency().getCurrency() + " (user = " + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        BigDecimal quoteForPlnCurrency = plnQuote.divide(newestQuote, SCALE, RoundingMode.HALF_DOWN);
        BigDecimal newestValue = quoteForPlnCurrency.multiply(deposit.getBoughtSum());
        if(deposit.getSoldCurrency() == Currency.PLN) {
            return newestValue.subtract(deposit.getSoldSum()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return deposit.getSoldSum().subtract(newestValue).setScale(2, RoundingMode.HALF_DOWN);
    }

    private Profit buildProfit(Deposit deposit, BigDecimal profit) {
        return Profit.builder()
                .depositId(deposit.getId())
                .profit(profit)
                .boughtCurrency(deposit.getBoughtCurrency())
                .soldCurrency(deposit.getSoldCurrency())
                .soldSum(deposit.getSoldSum())
                .build();
    }
}
