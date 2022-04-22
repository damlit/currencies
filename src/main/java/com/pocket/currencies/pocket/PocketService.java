package com.pocket.currencies.pocket;

import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.DepositDto;
import com.pocket.currencies.pocket.entity.ProfitDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PocketService {

    String addDeposit(DepositDto depositDto);
    String removeDeposit(long id);
    ProfitDto calculateProfit();
    Page<Deposit> getAllDepositsForCurrentUser(Pageable pageable);
    Integer getAmountOfDepositsForCurrentUser();
}
