package com.pocket.currencies.pocket.calculator;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.mock.MockUtils;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Pocket;
import com.pocket.currencies.pocket.entity.Profit;
import com.pocket.currencies.pocket.entity.ProfitDto;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProfitCalculatorTest {

    private final BigDecimal PLN_QUOTE = BigDecimal.valueOf(4.08);

    ProfitCalculator profitCalculator;
    @Mock
    ExchangeQuoteRepository exchangeQuoteRepository;
    @Mock
    OneDepositProfitCalculator oneDepositProfitCalculator;
    @Mock
    QuoteForCurrencyCalculator quoteForCurrencyCalculator;

    ExchangeQuote exchangeQuote;

    @BeforeEach
    public void setup() {
        profitCalculator = new ProfitCalculator(exchangeQuoteRepository, quoteForCurrencyCalculator, oneDepositProfitCalculator);

        exchangeQuote = ExchangeQuote.builder().quotesDate(Date.valueOf(LocalDate.now())).source("USD").build();
        Quote quotePln = Quote.builder().quote(PLN_QUOTE).exchangeQuote(exchangeQuote).currency(Currency.PLN).build();
        exchangeQuote.setQuotes(Collections.singletonList(quotePln));
        when(exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc()).thenReturn(exchangeQuote);

        when(quoteForCurrencyCalculator.getQuoteValueForCurrency(exchangeQuote, Currency.PLN)).thenReturn(PLN_QUOTE);
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void shouldCalculateProfit() {
        Pocket pocket = new Pocket();
        Deposit deposit = MockUtils.createMockDeposit(Currency.EUR, Currency.PLN, BigDecimal.valueOf(4.4));
        Deposit deposit1 = MockUtils.createMockDeposit(Currency.USD, Currency.PLN, BigDecimal.valueOf(3.92));
        pocket.setDeposits(Arrays.asList(deposit, deposit1));
        when(oneDepositProfitCalculator.calculateProfitFromOneDeposit(exchangeQuote, deposit, PLN_QUOTE))
                .thenReturn(MockUtils.createMockProfit(Currency.EUR, Currency.PLN, 15.0));
        when(oneDepositProfitCalculator.calculateProfitFromOneDeposit(exchangeQuote, deposit1, PLN_QUOTE))
                .thenReturn(MockUtils.createMockProfit(Currency.USD, Currency.PLN, 10.0));
        ProfitDto expectedProfit = ProfitDto.builder()
                .depositsProfits(Arrays.asList(
                        MockUtils.createMockProfit(Currency.EUR, Currency.PLN, 15.0),
                        MockUtils.createMockProfit(Currency.USD, Currency.PLN, 10.0))
                )
                .profit(BigDecimal.valueOf(25.0))
                .build();

        ProfitDto profit = profitCalculator.calculateProfit(pocket);

        assertEquals(expectedProfit, profit);
    }
}
