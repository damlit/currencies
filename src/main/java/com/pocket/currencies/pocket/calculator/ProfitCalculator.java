package com.pocket.currencies.pocket.calculator;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Pocket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@AllArgsConstructor
@Component
public class ProfitCalculator {

    ExchangeQuoteRepository exchangeQuoteRepository;

    public BigDecimal calculateProfit(Pocket pocket) {
        ExchangeQuote newestExchangeQuote = exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc();
        BigDecimal plnQuote = getQuoteValueForCurrency(newestExchangeQuote, Currency.PLN);
        return pocket.getDeposits().stream()
                .map(deposit -> calculateProfitFromOneDeposit(newestExchangeQuote, deposit, plnQuote))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getQuoteValueForCurrency(ExchangeQuote newestExchangeQuote, Currency currency) {
        return getQuoteForCurrency(newestExchangeQuote, currency)
                .map(Quote::getQuote)
                .orElse(BigDecimal.ONE);
    }

    private Optional<Quote> getQuoteForCurrency(ExchangeQuote newestExchangeQuote, Currency currency) {
        return newestExchangeQuote.getQuotes().stream()
                .filter(quote -> quote.getCurrency() == currency)
                .findFirst();
    }

    private BigDecimal calculateProfitFromOneDeposit(ExchangeQuote newestExchangeQuote, Deposit deposit, BigDecimal plnQuote) {
        Optional<Quote> quoteForCurrency = getQuoteForCurrency(newestExchangeQuote, deposit.getBoughtCurrency());
        return quoteForCurrency
                .map(Quote::getQuote)
                .map(newestQuote -> calculateDifference(deposit, newestQuote, plnQuote))
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateDifference(Deposit deposit, BigDecimal newestQuote, BigDecimal plnQuote) {
        BigDecimal quoteForPlnCurrency = plnQuote.divide(newestQuote, RoundingMode.HALF_DOWN);
        BigDecimal newestValue = quoteForPlnCurrency.multiply(deposit.getBoughtSum());
        return newestValue.subtract(deposit.getSoldSum()).setScale(2, RoundingMode.HALF_DOWN);
    }
}
