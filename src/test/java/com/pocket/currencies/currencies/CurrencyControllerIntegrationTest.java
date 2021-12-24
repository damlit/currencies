package com.pocket.currencies.currencies;

import com.pocket.currencies.client.QuotesClient;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.currencies.repository.QuoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CurrencyControllerIntegrationTest {

    private static final String CURRENCY_ENDPOINT = "/api/v1/currencies";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ExchangeQuoteRepository exchangeQuoteRepository;
    @Autowired
    private QuoteRepository quoteRepository;
    @MockBean
    private QuotesClient quotesClient;

    @Test
    @WithMockUser
    public void testGetLastCurrencies() throws Exception {
        String expectedResponse = "{\"id\":1,\"quotesDate\":null,\"source\":null,\"quotes\":[]}";
        ExchangeQuote exchangeQuote = ExchangeQuote.builder().id(1).build();
        exchangeQuoteRepository.save(exchangeQuote);

        this.mvc.perform(get(CURRENCY_ENDPOINT + "/last")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    @WithMockUser
    public void testGetLastCurrenciesChooseByDate() throws Exception {
        String expectedResponse = "{\"id\":2,\"quotesDate\":2,\"source\":null,\"quotes\":[]}";
        ExchangeQuote exchangeQuote = ExchangeQuote.builder().id(1).quotesDate(new Date(1L)).build();
        ExchangeQuote exchangeQuote2 = ExchangeQuote.builder().id(2).quotesDate(new Date(2L)).build();
        exchangeQuoteRepository.save(exchangeQuote);
        exchangeQuoteRepository.save(exchangeQuote2);

        this.mvc.perform(get(CURRENCY_ENDPOINT + "/last")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    @WithMockUser
    public void testGetQuotes() throws Exception {
        String expectedResponse = "[{\"id\":2,\"quotesDate\":null,\"source\":null,\"quotes\":[]},"
                + "{\"id\":1,\"quotesDate\":null,\"source\":null,\"quotes\":[]}]";
        ExchangeQuote exchangeQuote = ExchangeQuote.builder().id(1).build();
        ExchangeQuote exchangeQuote2 = ExchangeQuote.builder().id(2).build();
        exchangeQuoteRepository.save(exchangeQuote);
        exchangeQuoteRepository.save(exchangeQuote2);

        this.mvc.perform(get(CURRENCY_ENDPOINT + "/quotes")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    @WithMockUser
    public void testUpdateQuotes() throws Exception {
        Path pathToJsonBodyResponse = Paths.get("src/test/resources/quotes-client-response.json");
        String responseBody = Files.readString(pathToJsonBodyResponse);
        when(quotesClient.getQuotesFromService()).thenReturn(responseBody);
        String expectedResponse = "Quotes updated!";

        this.mvc.perform(get(CURRENCY_ENDPOINT + "/update")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
        assertEquals(1, exchangeQuoteRepository.count());
        assertEquals(4, quoteRepository.count());
    }
}
