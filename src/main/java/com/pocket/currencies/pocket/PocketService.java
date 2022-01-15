package com.pocket.currencies.pocket;

import com.pocket.currencies.pocket.entity.DepositDto;

public interface PocketService {

    String addDeposit(DepositDto depositDto);
    String removeDeposit(long id);
    String calculateProfit();
    String getAllDepositsForCurrentUser(int page, int size);
    Integer getAmountOfDepositsForCurrentUser();
}
