package com.pocket.currencies.currencies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.currencies.client.QuotesService;
import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.currencies.exception.GetCurrenciesException;
import com.pocket.currencies.currencies.exception.UpdateCurrenciesFailedException;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final Logger LOG = LoggerFactory.getLogger("logger");
    private final BigDecimal USD_CURRENCY_QUOTE = BigDecimal.ONE;
    private final int SCALE = 6;

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

    public String getLastQuotes(Currency targetCurrency) {
        LOG.info("Getting last quotes from database (user=" + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        ObjectMapper objectMapper = new ObjectMapper();
        ExchangeQuote newestExchangeQuote = exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc();
        recalculateQuotesForOtherCurrency(newestExchangeQuote, targetCurrency);
        try {
            return objectMapper.writeValueAsString(newestExchangeQuote);
        } catch (JsonProcessingException e) {
            throw new GetCurrenciesException();
        }
    }

    public String getQuotes(Currency targetCurrency) {
        LOG.info("Getting last 10 quotes from database (user=" + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        ObjectMapper objectMapper = new ObjectMapper();
        List<ExchangeQuote> newestExchangeQuote = exchangeQuoteRepository.findTop10ByOrderByQuotesDateDesc();
        newestExchangeQuote.forEach(exchangeQuote -> recalculateQuotesForOtherCurrency(exchangeQuote, targetCurrency));
        try {
            return objectMapper.writeValueAsString(newestExchangeQuote);
        } catch (JsonProcessingException e) {
            throw new GetCurrenciesException();
        }
    }

    public ExchangeQuote getQuoteByDate(Date date, Currency targetCurrency) {
        LOG.info("Getting quotes by date (" + date + ") for target currency " + targetCurrency + "(user=" + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        ExchangeQuote exchangeQuote = exchangeQuoteRepository.getQuotesByDate(date);
        if (exchangeQuote != null) {
            recalculateQuotesForOtherCurrency(exchangeQuote, targetCurrency);
        }
        return exchangeQuote;
    }

    private void recalculateQuotesForOtherCurrency(ExchangeQuote exchangeQuote, Currency targetCurrency) {
         exchangeQuote.getQuotes()
                .forEach(quote -> changeQuote(exchangeQuote, targetCurrency, quote));
         exchangeQuote.getQuoteForCurrency(targetCurrency)
                 .ifPresent(quote -> quote.setQuote(BigDecimal.ONE));
    }

    private void changeQuote(ExchangeQuote exchangeQuote, Currency targetCurrency, Quote quote) {
        exchangeQuote.getQuoteForCurrency(targetCurrency)
                .ifPresent(targetQuote -> quote.setQuote(calculateNewQuote(targetQuote, quote)));
    }

    private BigDecimal calculateNewQuote(Quote targetQuote, Quote quote) {
        if (quote.getCurrency().equals(targetQuote.getCurrency())) {
            return targetQuote.getQuote();
        }
        return targetQuote.getQuote()
                .multiply(USD_CURRENCY_QUOTE.divide(quote.getQuote(), SCALE, RoundingMode.CEILING))
                .setScale(SCALE, RoundingMode.CEILING);
    }
}
