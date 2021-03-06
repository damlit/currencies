package com.pocket.currencies.currencies.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "exchange_quote")
public class ExchangeQuote {

    @Id
    @GeneratedValue
    private long id;
    @Column(name = "quotes_date")
    private Date quotesDate;
    @Column
    private String source;
    @OneToMany(mappedBy = "exchangeQuote", fetch = FetchType.LAZY)
    private List<Quote> quotes;

    public Optional<Quote> getQuoteForCurrency(Currency currency) {
        return quotes.stream()
                .filter(quote -> currency.equals(quote.getCurrency()))
                .findFirst();
    }
}
