package com.pocket.currencies.currencies.repository;

import com.pocket.currencies.currencies.entity.ExchangeQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExchangeQuoteRepository extends JpaRepository<ExchangeQuote, Long> {

    ExchangeQuote findFirstByOrderByQuotesDateDesc();
    List<ExchangeQuote> findTop10ByOrderByQuotesDateDesc();

    @Query(value = "SELECT * FROM exchange_quote eq " +
            "JOIN quote q ON eq.id = q.exchange_quote_id " +
            "WHERE DATE(quotes_date) = DATE(?1)",
    nativeQuery = true)
    ExchangeQuote getQuotesByDate(Date quotesDate);
}
