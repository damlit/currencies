package com.pocket.currencies.client;

import com.fasterxml.jackson.core.JsonParseException;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.exception.UpdateCurrenciesFailedException;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.currencies.repository.QuoteRepository;
import com.pocket.currencies.users.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class QuotesServiceTest {

    private QuotesService quotesService;
    @Mock
    private ExchangeQuoteRepository exchangeQuoteRepository;
    @Mock
    private QuoteRepository quoteRepository;
    @Mock
    private QuotesClient quotesClient;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        quotesService = new QuotesServiceImpl(exchangeQuoteRepository, quoteRepository, quotesClient, userService);
    }

    @Test
    public void shouldParseResponseToExchangeQuote() throws IOException {
        Path pathToJsonBodyResponse = Paths.get("src/test/resources/quotes-client-response.json");
        String responseBody = Files.readString(pathToJsonBodyResponse);
        when(userService.isAdminUser()).thenReturn(true);
        when(quotesClient.getQuotesFromService()).thenReturn(responseBody);

        quotesService.updateQuotes();

        verify(quoteRepository, times(1)).saveAll(anyList());
        verify(exchangeQuoteRepository, times(1)).save(any());
    }

    @Test
    public void shouldThrowExceptionWhenDataIsNotJson() throws IOException {
        when(quotesClient.getQuotesFromService()).thenReturn("TEST");

        Assertions.assertThrows(JsonParseException.class, () -> quotesService.updateQuotes());
    }

    @Test
    public void shouldThrowExceptionWhenTheseQuotesExists() throws IOException {
        Path pathToJsonBodyResponse = Paths.get("src/test/resources/quotes-client-response.json");
        String responseBody = Files.readString(pathToJsonBodyResponse);
        when(quotesClient.getQuotesFromService()).thenReturn(responseBody);
        when(exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc()).thenReturn(createExchangeQuote());
        when(userService.isAdminUser()).thenReturn(true);

        Assertions.assertThrows(UpdateCurrenciesFailedException.class, () -> quotesService.updateQuotes());
    }

    @Test
    public void shouldThrowExceptionWhenUserIsNotAdmin() throws IOException {
        Path pathToJsonBodyResponse = Paths.get("src/test/resources/quotes-client-response.json");
        String responseBody = Files.readString(pathToJsonBodyResponse);
        when(quotesClient.getQuotesFromService()).thenReturn(responseBody);
        when(exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc()).thenReturn(createExchangeQuote());
        when(userService.isAdminUser()).thenReturn(false);

        Assertions.assertThrows(UpdateCurrenciesFailedException.class, () -> quotesService.updateQuotes());
    }

    private ExchangeQuote createExchangeQuote() {
        return ExchangeQuote.builder()
                .quotesDate(new Date(1639296603000L))
                .id(0)
                .source("USD")
                .build();
    }
}
