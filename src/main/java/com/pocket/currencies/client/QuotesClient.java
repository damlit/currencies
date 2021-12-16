package com.pocket.currencies.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.currencies.client.entity.QuotesResponse;
import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.currencies.exception.UpdateCurrenciesFailedException;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.currencies.repository.QuoteRepository;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class QuotesClient {

    private final ExchangeQuoteRepository exchangeQuoteRepository;
    private final QuoteRepository quoteRepository;
    private final Environment environment;

    private static final Integer TIMESTAMP_MULTIPLIER = 1000;
    private static final String URL = "quotes.client.url";
    private static final String ENDPOINT = "quotes.client.endpoint";
    private static final String KEY = "quotes.client.key";

    @Transactional
    public void updateQuotes() throws IOException {
        String response = getQuotesFromService();
        QuotesResponse quotesResponse = parseToObject(response);

        ExchangeQuote exchangeQuote = createExchangeQuoteFromResponse(quotesResponse);
        if (isNotQuotesFromLastUpdate(exchangeQuote)) {
            List<Quote> quotes = createQuotesFromResponse(quotesResponse, exchangeQuote);
            quoteRepository.saveAll(quotes);
            exchangeQuote.setQuotes(quotes);
            exchangeQuoteRepository.save(exchangeQuote);
        } else {
            throw new UpdateCurrenciesFailedException();
        }
    }

    private String getQuotesFromService() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getEndpoint())
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String getEndpoint() {
        return environment.getProperty(URL) + "/"
                + environment.getProperty(ENDPOINT)
                + "?access_key=" + environment.getProperty(KEY);
    }

    private QuotesResponse parseToObject(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonResponse, QuotesResponse.class);
    }

    private ExchangeQuote createExchangeQuoteFromResponse(QuotesResponse quotesResponse) {
        return ExchangeQuote.builder()
                .source(quotesResponse.getSource())
                .quotesDate(getDateFromTimestamp(quotesResponse.getTimestamp()))
                .build();
    }

    private Date getDateFromTimestamp(Long timestamp) {
        return new Date(timestamp * TIMESTAMP_MULTIPLIER);
    }

    private boolean isNotQuotesFromLastUpdate(ExchangeQuote exchangeQuote) {
        ExchangeQuote lastExchangeQuote = exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc();
        return lastExchangeQuote == null
                || lastExchangeQuote.getQuotesDate().getTime() != exchangeQuote.getQuotesDate().getTime();
    }

    private List<Quote> createQuotesFromResponse(QuotesResponse quotesResponse, ExchangeQuote exchangeQuote) {
        return quotesResponse.getQuotes().entrySet().stream()
                .filter(quote -> Currency.containsCurrency(quote.getKey().substring(3)))
                .map(quote -> createQuoteFromResponse(quote, exchangeQuote))
                .collect(Collectors.toList());
    }

    private Quote createQuoteFromResponse(Map.Entry<String, BigDecimal> quoteEntry, ExchangeQuote exchangeQuote) {
        return Quote.builder()
                .exchangeQuote(exchangeQuote)
                .quote(quoteEntry.getValue())
                .currency(Currency.valueOf(quoteEntry.getKey().substring(3)))
                .build();
    }
}
