package com.pocket.currencies.currencies;

import com.pocket.currencies.client.QuotesService;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.*;

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

        currencyService.getLastQuotes();

        verify(exchangeQuoteRepository, times(1)).findFirstByOrderByQuotesDateDesc();
    }

    @Test
    @WithMockUser
    public void shouldGetLast10Quotes() {

        currencyService.getQuotes();

        verify(exchangeQuoteRepository, times(1)).findTop10ByOrderByQuotesDateDesc();
    }
}
