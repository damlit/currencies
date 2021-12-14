package com.pocket.currencies.currencies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.currencies.client.QuotesClient;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final QuotesClient client;
    private final ExchangeQuoteRepository exchangeQuoteRepository;

    public boolean updateCurrencies() {
        try {
            client.updateQuotes();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getLastCurrencies() {
        ObjectMapper objectMapper = new ObjectMapper();
        ExchangeQuote newestExchangeQuote = exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc();
        try {
            return objectMapper.writeValueAsString(newestExchangeQuote);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "JsonProcessingException";
        }
    }
}
