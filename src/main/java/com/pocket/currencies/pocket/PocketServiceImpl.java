package com.pocket.currencies.pocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.currencies.pocket.calculator.ProfitCalculator;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.DepositDto;
import com.pocket.currencies.pocket.entity.Pocket;
import com.pocket.currencies.pocket.exception.GetDepositsException;
import com.pocket.currencies.pocket.exception.IncorrectInputDataException;
import com.pocket.currencies.pocket.repository.DepositRepository;
import com.pocket.currencies.pocket.repository.PocketRepository;
import com.pocket.currencies.users.UserService;
import com.pocket.currencies.users.entity.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class PocketServiceImpl implements PocketService {

    private final Logger LOG = LoggerFactory.getLogger("logger");

    private final static String SUCCESS_DEPOSIT_MSG = "Success deposit with id %s";
    private final static String REMOVE_DEPOSIT_MSG = "Deposit with id %s has been removed";

    private final PocketRepository pocketRepository;
    private final DepositRepository depositRepository;
    private final UserService userService;
    private final ProfitCalculator profitCalculator;

    @Override
    @Transactional
    public String addDeposit(DepositDto depositDto) {
        try {
            Deposit deposit = createDepositFromDto(depositDto);
            LOG.info("Adding deposit (id=" + deposit.getId() + ") to pocket (id=" + deposit.getPocket().getId() + ")");
            depositRepository.save(deposit);
            return String.format(SUCCESS_DEPOSIT_MSG, deposit.getId());
        } catch (IllegalArgumentException exception) {
            throw new IncorrectInputDataException();
        }
    }

    @Override
    public String removeDeposit(long id) {
        depositRepository.deleteById(id);
        LOG.info("Removing deposit (id=" + id + ")");
        return String.format(REMOVE_DEPOSIT_MSG, id);
    }

    @Override
    public String calculateProfit() {
        LOG.info("Start calculating profit (user = " + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        BigDecimal profit = profitCalculator.calculateProfit(getActiveUserPocket());
        LOG.info("Profit = " + profit.toPlainString() + " (user = " + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        return profit.toPlainString();
    }

    @Override
    public String getAllDepositsForCurrentUser(int page, int size) {
        LOG.info("Getting deposits (user=" + SecurityContextHolder.getContext().getAuthentication().getName() + ")");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Deposit> deposits = depositRepository.getAllByPocket(getActiveUserPocket(), PageRequest.of(page, size));
        try {
            return objectMapper.writeValueAsString(deposits);
        } catch (JsonProcessingException e) {
            throw new GetDepositsException();
        }
    }

    private Deposit createDepositFromDto(DepositDto depositDto) {
        Pocket pocket = getActiveUserPocket();
        return Deposit.builder()
                .pocket(pocket)
                .boughtCurrency(depositDto.getBoughtCurrency())
                .soldCurrency(depositDto.getSoldCurrency())
                .boughtSum(calculateBoughtSumWithPrecision(depositDto))
                .soldSum(depositDto.getSoldSum())
                .quote(depositDto.getQuote())
                .build();
    }

    private Pocket getActiveUserPocket() {
        User user = userService.getActiveUser();
        return pocketRepository.getById(user.getPocket().getId());
    }

    private BigDecimal calculateBoughtSumWithPrecision(DepositDto depositDto) {
        BigDecimal soldSumWithScale = depositDto.getSoldSum().setScale(3, RoundingMode.HALF_DOWN);
        return soldSumWithScale.divide(depositDto.getQuote(), RoundingMode.HALF_DOWN);
    }
}
