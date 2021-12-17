package com.pocket.currencies.currencies.repository;

import com.pocket.currencies.currencies.entity.ExchangeQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeQuoteRepository extends JpaRepository<ExchangeQuote, Long> {

    ExchangeQuote findFirstByOrderByQuotesDateDesc();
    List<ExchangeQuote> findTop10ByOrderByQuotesDateDesc();
}
