package com.pocket.currencies.currencies.repository;

import com.pocket.currencies.currencies.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
