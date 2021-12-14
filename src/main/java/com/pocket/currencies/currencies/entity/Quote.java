package com.pocket.currencies.currencies.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
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

}
