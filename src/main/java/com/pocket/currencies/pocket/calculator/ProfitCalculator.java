package com.pocket.currencies.pocket.calculator;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Pocket;
import com.pocket.currencies.pocket.entity.Profit;
import com.pocket.currencies.pocket.entity.ProfitDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ProfitCalculator {

    private final Logger LOG = LoggerFactory.getLogger("logger");

    private final ExchangeQuoteRepository exchangeQuoteRepository;
    private final QuoteForCurrencyCalculator quoteForCurrencyCalculator;
    private final OneDepositProfitCalculator oneDepositProfitCalculator;

    public ProfitDto calculateProfit(Pocket pocket) {
        ExchangeQuote newestExchangeQuote = exchangeQuoteRepository.findFirstByOrderByQuotesDateDesc();

        BigDecimal plnQuote = quoteForCurrencyCalculator.getQuoteValueForCurrency(newestExchangeQuote, Currency.PLN);
        LOG.info("PLN quote for source quote (USD) = " + plnQuote);

        List<Profit> depositsProfits = getDepositsProfits(pocket, newestExchangeQuote, plnQuote);

        return ProfitDto.builder()
                .depositsProfits(depositsProfits)
                .profit(getSummaryProfit(depositsProfits))
                .build();
    }

    private List<Profit> getDepositsProfits(Pocket pocket, ExchangeQuote exchangeQuote, BigDecimal plnQuote) {
        return pocket.getDeposits().stream()
                .map(deposit -> oneDepositProfitCalculator.calculateProfitFromOneDeposit(exchangeQuote, deposit, plnQuote))
                .collect(Collectors.toList());
    }

    private BigDecimal getSummaryProfit(List<Profit> depositsProfits) {
        return depositsProfits.stream()
                .map(Profit::getProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
