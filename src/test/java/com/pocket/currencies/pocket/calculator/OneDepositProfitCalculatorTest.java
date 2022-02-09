package com.pocket.currencies.pocket.calculator;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.mock.MockUtils;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Profit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OneDepositProfitCalculatorTest {

    private final BigDecimal PLN_QUOTE = BigDecimal.valueOf(4.08);

    OneDepositProfitCalculator oneDepositProfitCalculator;
    @Mock
    QuoteForCurrencyCalculator quoteForCurrencyCalculator;
    ExchangeQuote exchangeQuote;

    @BeforeEach
    public void setup() {
        oneDepositProfitCalculator = new OneDepositProfitCalculator(quoteForCurrencyCalculator);

        exchangeQuote = ExchangeQuote.builder().quotesDate(Date.valueOf(LocalDate.now())).source("USD").build();
        Quote quoteEur = Quote.builder().quote(BigDecimal.valueOf(0.88)).exchangeQuote(exchangeQuote).currency(Currency.EUR).build();
        Quote quotePln = Quote.builder().quote(PLN_QUOTE).exchangeQuote(exchangeQuote).currency(Currency.PLN).build();
        Quote quoteUsd = Quote.builder().quote(BigDecimal.ONE).exchangeQuote(exchangeQuote).currency(Currency.USD).build();
        exchangeQuote.setQuotes(Arrays.asList(quoteEur, quotePln, quoteUsd));

        Quote quote = Quote.builder().quote(BigDecimal.valueOf(4.61)).currency(Currency.PLN).build();
        when(quoteForCurrencyCalculator.getQuoteForCurrency(exchangeQuote, Currency.EUR)).thenReturn(Optional.of(quote));
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateProfitForCurrency() {
        Deposit deposit = MockUtils.createMockDeposit(Currency.PLN, Currency.EUR, BigDecimal.valueOf(4.9));
        BigDecimal expectedProfit = BigDecimal.valueOf(8.23).setScale(2, RoundingMode.HALF_DOWN);

        Profit profit = oneDepositProfitCalculator.calculateProfitFromOneDeposit(exchangeQuote, deposit, PLN_QUOTE);

        assertEquals(expectedProfit, profit.getProfit());
        assertEquals(Currency.PLN, profit.getBoughtCurrency());
        assertEquals(Currency.EUR, profit.getSoldCurrency());
        assertEquals(BigDecimal.TEN, profit.getSoldSum());
        assertEquals(100, profit.getDepositId());
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateProfitForReverseCurrency() {
        Deposit deposit = MockUtils.createMockDeposit(Currency.EUR, Currency.PLN, BigDecimal.valueOf(4.9));
        BigDecimal expectedProfit = BigDecimal.valueOf(-8.23).setScale(2, RoundingMode.HALF_DOWN);

        Profit profit = oneDepositProfitCalculator.calculateProfitFromOneDeposit(exchangeQuote, deposit, PLN_QUOTE);

        assertEquals(expectedProfit, profit.getProfit());
        assertEquals(Currency.EUR, profit.getBoughtCurrency());
        assertEquals(Currency.PLN, profit.getSoldCurrency());
        assertEquals(BigDecimal.TEN, profit.getSoldSum());
        assertEquals(100, profit.getDepositId());
    }
}
