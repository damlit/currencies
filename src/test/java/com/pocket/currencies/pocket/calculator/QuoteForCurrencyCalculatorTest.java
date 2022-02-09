package com.pocket.currencies.pocket.calculator;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuoteForCurrencyCalculatorTest {

    QuoteForCurrencyCalculator quoteForCurrencyCalculator;
    ExchangeQuote exchangeQuote;

    @BeforeEach
    public void setup() {
        quoteForCurrencyCalculator = new QuoteForCurrencyCalculator();
        exchangeQuote = ExchangeQuote.builder().quotesDate(Date.valueOf(LocalDate.now())).source("USD").build();
        Quote quoteEur = Quote.builder().quote(BigDecimal.valueOf(0.88)).exchangeQuote(exchangeQuote).currency(Currency.EUR).build();
        Quote quotePln = Quote.builder().quote(BigDecimal.valueOf(4.08)).exchangeQuote(exchangeQuote).currency(Currency.PLN).build();
        Quote quoteUsd = Quote.builder().quote(BigDecimal.ONE).exchangeQuote(exchangeQuote).currency(Currency.USD).build();
        exchangeQuote.setQuotes(Arrays.asList(quoteEur, quotePln, quoteUsd));
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateQuoteForCurrency() {
        Quote expectedQuote = Quote.builder().quote(BigDecimal.valueOf(4.08)).currency(Currency.PLN).build();

        Optional<Quote> quote = quoteForCurrencyCalculator.getQuoteForCurrency(exchangeQuote, Currency.PLN);

        assertTrue(quote.isPresent());
        assertEquals(expectedQuote.getQuote(), quote.get().getQuote());
        assertEquals(expectedQuote.getCurrency(), quote.get().getCurrency());
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateQuoteForOtherCurrency() {
        Quote expectedQuote = Quote.builder().quote(BigDecimal.valueOf(0.88)).currency(Currency.EUR).build();

        Optional<Quote> quote = quoteForCurrencyCalculator.getQuoteForCurrency(exchangeQuote, Currency.EUR);

        assertTrue(quote.isPresent());
        assertEquals(expectedQuote.getQuote(), quote.get().getQuote());
        assertEquals(expectedQuote.getCurrency(), quote.get().getCurrency());
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateValueForCurrency() {
        BigDecimal expectedQuoteValue = BigDecimal.valueOf(4.08);

        BigDecimal quoteValue = quoteForCurrencyCalculator.getQuoteValueForCurrency(exchangeQuote, Currency.PLN);

        assertEquals(expectedQuoteValue, quoteValue);
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateValueForOtherCurrency() {
        BigDecimal expectedQuoteValue = BigDecimal.valueOf(0.88);

        BigDecimal quoteValue = quoteForCurrencyCalculator.getQuoteValueForCurrency(exchangeQuote, Currency.EUR);

        assertEquals(expectedQuoteValue, quoteValue);
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldReturnOneWhenQuotesDoesNotExist() {
        ExchangeQuote exchangeQuote = ExchangeQuote.builder().quotes(new ArrayList<>()).build();
        BigDecimal expectedQuoteValue = BigDecimal.ONE;

        BigDecimal quoteValue = quoteForCurrencyCalculator.getQuoteValueForCurrency(exchangeQuote, Currency.EUR);

        assertEquals(expectedQuoteValue, quoteValue);
    }
}
