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
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class QuotesServiceImpl implements QuotesService {

    private final Logger LOG = LoggerFactory.getLogger("logger");

    private final ExchangeQuoteRepository exchangeQuoteRepository;
    private final QuoteRepository quoteRepository;
    private final QuotesClient quotesClient;

    private static final Integer TIMESTAMP_MULTIPLIER = 1000;

    @Transactional
    public void updateQuotes() throws IOException {
        String response = quotesClient.getQuotesFromService();
        QuotesResponse quotesResponse = parseToObject(response);

        ExchangeQuote exchangeQuote = createExchangeQuoteFromResponse(quotesResponse);
        if (isNotQuotesFromLastUpdate(exchangeQuote)) {
            LOG.info("Saving new quotes");
            List<Quote> quotes = createQuotesFromResponse(quotesResponse, exchangeQuote);
            quoteRepository.saveAll(quotes);
            exchangeQuote.setQuotes(quotes);
            exchangeQuoteRepository.save(exchangeQuote);
        } else {
            LOG.info("Quotes are updated already");
            throw new UpdateCurrenciesFailedException();
        }
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
        LOG.info("Checking if this quotes (time: " + exchangeQuote.getQuotesDate() + ") exists in database");
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
