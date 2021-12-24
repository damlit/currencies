package com.pocket.currencies.pocket.calculator;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Pocket;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProfitCalculatorTest {

    ProfitCalculator profitCalculator;
    @Mock
    ExchangeQuoteRepository exchangeQuoteRepository;

    ExchangeQuote exchangeQuote;

    @BeforeEach
    public void setup() {
        profitCalculator = new ProfitCalculator(exchangeQuoteRepository);
        exchangeQuote = ExchangeQuote.builder().quotesDate(Date.valueOf(LocalDate.now())).source("USD").build();
        Quote quoteEur = Quote.builder().quote(BigDecimal.valueOf(0.88)).exchangeQuote(exchangeQuote).currency(Currency.EUR).build();
        Quote quotePln = Quote.builder().quote(BigDecimal.valueOf(4.08)).exchangeQuote(exchangeQuote).currency(Currency.PLN).build();
        Quote quoteUsd = Quote.builder().quote(BigDecimal.ONE).exchangeQuote(exchangeQuote).currency(Currency.USD).build();
        exchangeQuote.setQuotes(Arrays.asList(quoteEur, quotePln, quoteUsd));
        when(exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc()).thenReturn(exchangeQuote);
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateProfit() {
        Pocket pocket = new Pocket();
        Deposit deposit = createMockDeposit(Currency.EUR, Currency.PLN, BigDecimal.valueOf(4.4));
        Deposit deposit1 = createMockDeposit(Currency.USD, Currency.PLN, BigDecimal.valueOf(3.92));
        pocket.setDeposits(Arrays.asList(deposit, deposit1));

        BigDecimal profit = profitCalculator.calculateProfit(pocket);

        assertEquals("1.52", profit.toPlainString());
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateProfitOnMinus() {
        Pocket pocket = new Pocket();
        Deposit deposit = createMockDeposit(Currency.EUR, Currency.PLN, BigDecimal.valueOf(5.4));
        Deposit deposit1 = createMockDeposit(Currency.USD, Currency.PLN, BigDecimal.valueOf(4.92));
        pocket.setDeposits(Arrays.asList(deposit, deposit1));

        BigDecimal profit = profitCalculator.calculateProfit(pocket);

        assertEquals("-2.56", profit.toPlainString());
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateProfitWithReverseCurrencies() {
        Pocket pocket = new Pocket();
        Deposit deposit = createMockDeposit(Currency.PLN, Currency.EUR, BigDecimal.valueOf(5.4));
        Deposit deposit1 = createMockDeposit(Currency.PLN, Currency.USD, BigDecimal.valueOf(4.92));
        pocket.setDeposits(Arrays.asList(deposit, deposit1));

        BigDecimal profit = profitCalculator.calculateProfit(pocket);

        assertEquals("2.56", profit.toPlainString());
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateProfitWithBothTypeOfDeposits() {
        Pocket pocket = new Pocket();
        Deposit deposit = createMockDeposit(Currency.EUR, Currency.PLN, BigDecimal.valueOf(5.4));
        Deposit deposit1 = createMockDeposit(Currency.PLN, Currency.USD, BigDecimal.valueOf(4.92));
        pocket.setDeposits(Arrays.asList(deposit, deposit1));

        BigDecimal profit = profitCalculator.calculateProfit(pocket);

        assertEquals("1.12", profit.toPlainString());
    }

    private Deposit createMockDeposit(Currency boughtCurrency, Currency soldCurrency, BigDecimal quote) {
        return Deposit.builder()
                .soldCurrency(soldCurrency)
                .boughtCurrency(boughtCurrency)
                .quote(quote)
                .pocket(null)
                .soldSum(BigDecimal.TEN)
                .boughtSum(BigDecimal.TEN.divide(quote, RoundingMode.HALF_DOWN))
                .build();
    }
}
