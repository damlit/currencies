package com.pocket.currencies.currencies;

import com.pocket.currencies.client.QuotesService;
import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CurrencyServiceTest {

    private CurrencyService currencyService;
    @Mock
    private QuotesService quotesService;
    @Mock
    private ExchangeQuoteRepository exchangeQuoteRepository;

    @BeforeEach
    public void setup() {
        currencyService = new CurrencyServiceImpl(quotesService, exchangeQuoteRepository);
    }

    @Test
    @WithMockUser
    public void shouldGetLastQuotes() {
        ExchangeQuote expectedExchangeQuote = new ExchangeQuote(1, new Date(1L), "USD", new ArrayList<>());
        when(exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc()).thenReturn(expectedExchangeQuote);

        ExchangeQuote exchangeQuote = currencyService.getLastQuotes(Currency.USD);

        verify(exchangeQuoteRepository, times(1)).findFirstByOrderByQuotesDateDesc();
        assertEquals(expectedExchangeQuote, exchangeQuote);
    }

    @Test
    @WithMockUser
    public void shouldGetLast10Quotes() {
        List<ExchangeQuote> expectedExchangeQuote = Collections.singletonList(new ExchangeQuote(1, new Date(1L), "USD", new ArrayList<>()));
        when(exchangeQuoteRepository.findTop10ByOrderByQuotesDateDesc()).thenReturn(expectedExchangeQuote);

        List<ExchangeQuote> exchangeQuotes = currencyService.getQuotes(Currency.USD);

        verify(exchangeQuoteRepository, times(1)).findTop10ByOrderByQuotesDateDesc();
        assertEquals(expectedExchangeQuote, exchangeQuotes);
    }

    @Test
    @WithMockUser
    public void shouldReturnQuotesWhenGetLastQuotesByDate() {
        Date date = new Date(1L);
        ExchangeQuote expectedExchangeQuote = new ExchangeQuote(1, date, "USD", new ArrayList<>());
        when(exchangeQuoteRepository.getQuotesByDate(date)).thenReturn(expectedExchangeQuote);

        ExchangeQuote exchangeQuote = currencyService.getQuoteByDate(date, Currency.USD);

        verify(exchangeQuoteRepository, times(1)).getQuotesByDate(date);
        assertEquals(expectedExchangeQuote, exchangeQuote);
    }

    @Test
    @WithMockUser
    public void shouldReturnEmptyResponseWhenGetLastQuotesByDateForNotExistingDate() {
        Date date = new Date(2L);
        when(exchangeQuoteRepository.getQuotesByDate(date)).thenReturn(null);

        ExchangeQuote exchangeQuote = currencyService.getQuoteByDate(new Date(2L), Currency.USD);

        verify(exchangeQuoteRepository, times(1)).getQuotesByDate(date);
        assertNull(exchangeQuote);
    }
}
