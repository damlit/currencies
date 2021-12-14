package com.pocket.currencies.pocket.entity;

import com.pocket.currencies.currencies.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Entity
@Table(name = "deposit")
public class Deposit {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private Currency soldCurrency;
    @Column
    private Currency boughtCurrency;
    @Column
    private BigDecimal quote;
    @Column
    private BigDecimal soldSum;
    @Column
    private BigDecimal boughtSum;
    @ManyToOne
    @JoinColumn(name = "pocket_id")
    private Pocket pocket;
}
