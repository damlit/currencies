package com.pocket.currencies.currencies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.currencies.client.QuotesClient;
import com.pocket.currencies.client.QuotesService;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.exception.GetCurrenciesException;
import com.pocket.currencies.currencies.exception.UpdateCurrenciesFailedException;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final Logger LOG = LoggerFactory.getLogger("logger");

    private final QuotesService quotesService;
    private final ExchangeQuoteRepository exchangeQuoteRepository;

    public boolean updateCurrencies() {
        try {
            quotesService.updateQuotes();
            return true;
        } catch (IOException e) {
            throw new UpdateCurrenciesFailedException();
        }
    }

    public String getLastQuotes() {
        LOG.info("Getting last quotes from database (user=" + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        ObjectMapper objectMapper = new ObjectMapper();
        ExchangeQuote newestExchangeQuote = exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc();
        try {
            return objectMapper.writeValueAsString(newestExchangeQuote);
        } catch (JsonProcessingException e) {
            throw new GetCurrenciesException();
        }
    }

    public String getQuotes() {
        LOG.info("Getting last 10 quotes from database (user=" + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        ObjectMapper objectMapper = new ObjectMapper();
        List<ExchangeQuote> newestExchangeQuote = exchangeQuoteRepository.findTop10ByOrderByQuotesDateDesc();
        try {
            return objectMapper.writeValueAsString(newestExchangeQuote);
        } catch (JsonProcessingException e) {
            throw new GetCurrenciesException();
        }
    }
}
