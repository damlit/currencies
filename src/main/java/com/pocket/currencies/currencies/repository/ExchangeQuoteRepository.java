package com.pocket.currencies.currencies.repository;

import com.pocket.currencies.currencies.entity.ExchangeQuote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeQuoteRepository extends JpaRepository<ExchangeQuote, Long> {

    ExchangeQuote findFirstByOrderByQuotesDateDesc();
    List<ExchangeQuote> findTop10ByOrderByQuotesDateDesc();
}
