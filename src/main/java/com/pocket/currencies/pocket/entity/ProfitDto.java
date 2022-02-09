package com.pocket.currencies.pocket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfitDto {

    public BigDecimal profit;
    public List<Profit> depositsProfits;
}
