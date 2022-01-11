package com.pocket.currencies.currencies.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.FetchType;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "quote")
public class Quote {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private Currency currency;
    @Column(name = "quote", columnDefinition = "Numeric(15,6)")
    private BigDecimal quote;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_quote_id")
    private ExchangeQuote exchangeQuote;

    public void setQuote(BigDecimal quote) {
        this.quote = quote;
    }
}
