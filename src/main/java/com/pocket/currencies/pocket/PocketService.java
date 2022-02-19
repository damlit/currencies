package com.pocket.currencies.pocket;

import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.DepositDto;
import com.pocket.currencies.pocket.entity.ProfitDto;

import java.util.List;

public interface PocketService {

    String addDeposit(DepositDto depositDto);
    String removeDeposit(long id);
    ProfitDto calculateProfit();
    List<Deposit> getAllDepositsForCurrentUser(int page, int size);
    Integer getAmountOfDepositsForCurrentUser();
}
