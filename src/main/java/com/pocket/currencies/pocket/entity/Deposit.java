package com.pocket.currencies.pocket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pocket.currencies.currencies.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "deposit")
public class Deposit {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private Currency soldCurrency;
    @Column
    private Currency boughtCurrency;
    @Column(name = "quote", columnDefinition = "Numeric(15,6)")
    private BigDecimal quote;
    @Column(name = "soldSum", columnDefinition = "Numeric(19,2)")
    private BigDecimal soldSum;
    @Column(name = "boughtSum", columnDefinition = "Numeric(19,2)")
    private BigDecimal boughtSum;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pocket_id")
    private Pocket pocket;
}
