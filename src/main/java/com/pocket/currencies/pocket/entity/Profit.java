package com.pocket.currencies.pocket.entity;

import com.pocket.currencies.currencies.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Builder
public class Profit {

    private long depositId;
    private BigDecimal profit;
    private BigDecimal soldSum;
    private Currency soldCurrency;
    private Currency boughtCurrency;
}
