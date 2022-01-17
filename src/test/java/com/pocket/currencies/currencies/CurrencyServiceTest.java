package com.pocket.currencies.currencies;

import com.pocket.currencies.client.QuotesService;
import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.users.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        currencyService = new CurrencyServiceImpl(quotesService, exchangeQuoteRepository);
    }

    @Test
    @WithMockUser
    public void shouldGetLastQuotes() {
        when(exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc()).thenReturn(new ExchangeQuote(1, new Date(1L), "USD", new ArrayList<>()));
        String expectedMessage = "{\"id\":1,\"quotesDate\":1,\"source\":\"USD\",\"quotes\":[]}";

        String message = currencyService.getLastQuotes(Currency.USD);

        verify(exchangeQuoteRepository, times(1)).findFirstByOrderByQuotesDateDesc();
        assertEquals(expectedMessage, message);
    }

    @Test
    @WithMockUser
    public void shouldGetLast10Quotes() {
        ExchangeQuote exchangeQuote = new ExchangeQuote(1, new Date(1L), "USD", new ArrayList<>());
        String expectedMessage = "[{\"id\":1,\"quotesDate\":1,\"source\":\"USD\",\"quotes\":[]}]";
        when(exchangeQuoteRepository.findTop10ByOrderByQuotesDateDesc()).thenReturn(Collections.singletonList(exchangeQuote));

        String message = currencyService.getQuotes(Currency.USD);

        verify(exchangeQuoteRepository, times(1)).findTop10ByOrderByQuotesDateDesc();
        assertEquals(expectedMessage, message);
    }
}
