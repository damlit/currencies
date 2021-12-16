package com.pocket.currencies.pocket.entity;

import com.pocket.currencies.currencies.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepositDto {

    private Currency soldCurrency;
    private Currency boughtCurrency;
    private BigDecimal quote;
    private BigDecimal soldSum;
}
