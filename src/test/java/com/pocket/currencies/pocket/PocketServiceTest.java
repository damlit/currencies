package com.pocket.currencies.pocket;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.mock.MockUtils;
import com.pocket.currencies.pocket.calculator.ProfitCalculator;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.DepositDto;
import com.pocket.currencies.pocket.entity.Pocket;
import com.pocket.currencies.pocket.entity.ProfitDto;
import com.pocket.currencies.pocket.entity.Profit;
import com.pocket.currencies.pocket.repository.DepositRepository;
import com.pocket.currencies.pocket.repository.PocketRepository;
import com.pocket.currencies.users.UserService;
import com.pocket.currencies.users.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PocketServiceTest {

    private PocketService pocketService;
    @Mock
    private DepositRepository depositRepository;
    @Mock
    private PocketRepository pocketRepository;
    @Mock
    private UserService userService;
    @Mock
    private ProfitCalculator profitCalculator;

    @BeforeEach
    public void setup() {
        pocketService = new PocketServiceImpl(pocketRepository, depositRepository, userService, profitCalculator);
    }

    @Test
    public void shouldAddDeposit() {
        String expectedMessage = "Success deposit with id 0";
        DepositDto depositDto = new DepositDto(Currency.PLN, Currency.EUR, BigDecimal.TEN, BigDecimal.ONE);
        User user = MockUtils.getMockedUser();
        Pocket pocket = new Pocket(1, user, new ArrayList<>());
        user.setPocket(pocket);
        when(userService.getActiveUser()).thenReturn(user);
        when(pocketRepository.getById(1L)).thenReturn(pocket);

        String message = pocketService.addDeposit(depositDto);

        verify(depositRepository, times(1)).save(any());
        assertEquals(expectedMessage, message);
    }

    @Test
    public void shouldThrowExceptionForAddingDeposit() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new DepositDto(Currency.PLN, Currency.valueOf("TEST"), BigDecimal.TEN, BigDecimal.ONE));
    }

    @Test
    public void shouldRemoveDeposit() {
        String expectedMessage = "Deposit with id 1 has been removed";

        String message = pocketService.removeDeposit(1L);

        verify(depositRepository, times(1)).deleteById(1L);
        assertEquals(expectedMessage, message);
    }

    @Test
    @WithMockUser
    public void shouldReturnProfit() {
        User user = MockUtils.getMockedUser();
        Pocket pocket = new Pocket(1, user, new ArrayList<>());
        List<Deposit> deposits = new ArrayList<>(
                Collections.singletonList(new Deposit(3, Currency.PLN, Currency.EUR, BigDecimal.TEN, BigDecimal.valueOf(10), BigDecimal.valueOf(1), pocket))
        );
        pocket.setDeposits(deposits);
        user.setPocket(pocket);
        Profit expectedProfit = Profit.builder().depositId(10L).profit(BigDecimal.valueOf(10.21)).soldSum(BigDecimal.TEN).boughtCurrency(Currency.EUR).soldCurrency(Currency.PLN).build();
        ProfitDto profitDto = ProfitDto.builder().profit(BigDecimal.valueOf(10.21)).depositsProfits(Collections.singletonList(expectedProfit)).build();
        when(userService.getActiveUser()).thenReturn(user);
        when(pocketRepository.getById(1L)).thenReturn(pocket);
        when(profitCalculator.calculateProfit(pocket)).thenReturn(getMockedProfitDto());

        ProfitDto profit = pocketService.calculateProfit();

        verify(profitCalculator, times(1)).calculateProfit(pocket);
        assertEquals(profitDto, profit);
    }

    private ProfitDto getMockedProfitDto() {
        return ProfitDto.builder()
                .profit(BigDecimal.valueOf(10.21))
                .depositsProfits(
                        Collections.singletonList(MockUtils.createMockProfit(Currency.EUR, Currency.PLN, 10.21))
                )
                .build();
    }

    @Test
    @WithMockUser
    public void shouldReturnDeposits() {
        User user = MockUtils.getMockedUser();
        Pocket pocket = new Pocket(1, user, new ArrayList<>());
        Deposit deposit1 = new Deposit(1, Currency.PLN, Currency.EUR, BigDecimal.valueOf(10.0), BigDecimal.ONE, BigDecimal.TEN, pocket);
        Deposit deposit2 = new Deposit(2, Currency.PLN, Currency.EUR, BigDecimal.valueOf(10.0), BigDecimal.ONE, BigDecimal.TEN, pocket);
        Deposit deposit3 = new Deposit(3, Currency.PLN, Currency.EUR, BigDecimal.valueOf(10.0), BigDecimal.ONE, BigDecimal.TEN, pocket);
        List<Deposit> deposits = Arrays.asList(deposit1, deposit2, deposit3);
        pocket.setDeposits(deposits);
        user.setPocket(pocket);
        when(userService.getActiveUser()).thenReturn(user);
        when(pocketRepository.getById(1L)).thenReturn(pocket);
        when(depositRepository.getAllByPocket(pocket, Pageable.ofSize(3))).thenReturn(deposits);

        List<Deposit> depositsList = pocketService.getAllDepositsForCurrentUser(Pageable.ofSize(3));

        verify(depositRepository, times(1)).getAllByPocket(pocket, PageRequest.of(0, 3));
        assertEquals(deposits, depositsList);
    }
}
