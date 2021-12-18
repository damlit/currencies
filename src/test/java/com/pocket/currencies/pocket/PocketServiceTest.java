package com.pocket.currencies.pocket;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.pocket.calculator.ProfitCalculator;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.DepositDto;
import com.pocket.currencies.pocket.entity.Pocket;
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
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
        User user = getMockedUser();
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
        String expectedMessage = "1000.21";
        User user = getMockedUser();
        Pocket pocket = new Pocket(1, user, new ArrayList<>());
        user.setPocket(pocket);
        when(userService.getActiveUser()).thenReturn(user);
        when(pocketRepository.getById(1L)).thenReturn(pocket);
        when(profitCalculator.calculateProfit(pocket)).thenReturn(BigDecimal.valueOf(1000.21));

        String message = pocketService.calculateProfit();

        verify(profitCalculator, times(1)).calculateProfit(pocket);
        assertEquals(expectedMessage, message);
    }

    @Test
    @WithMockUser
    public void shouldReturnDeposits() {
        String expectedMessage = "["
                + "{\"id\":1,\"soldCurrency\":\"PLN\",\"boughtCurrency\":\"EUR\",\"quote\":10.0,\"soldSum\":1,\"boughtSum\":10},"
                + "{\"id\":2,\"soldCurrency\":\"PLN\",\"boughtCurrency\":\"EUR\",\"quote\":10.0,\"soldSum\":1,\"boughtSum\":10}"
                + "]";
        User user = getMockedUser();
        Pocket pocket = new Pocket(1, user, new ArrayList<>());
        Deposit deposit1 = new Deposit(1, Currency.PLN, Currency.EUR, BigDecimal.valueOf(10.0), BigDecimal.ONE, BigDecimal.TEN, pocket);
        Deposit deposit2 = new Deposit(2, Currency.PLN, Currency.EUR, BigDecimal.valueOf(10.0), BigDecimal.ONE, BigDecimal.TEN, pocket);
        List<Deposit> deposits = Arrays.asList(deposit1, deposit2);
        pocket.setDeposits(deposits);
        user.setPocket(pocket);
        when(userService.getActiveUser()).thenReturn(user);
        when(pocketRepository.getById(1L)).thenReturn(pocket);
        when(depositRepository.getAllByPocket(pocket)).thenReturn(deposits);

        String message = pocketService.getAllDepositsForCurrentUser();

        verify(depositRepository, times(1)).getAllByPocket(pocket);
        assertEquals(expectedMessage, message);
    }

    private User getMockedUser() {
        return User.builder()
                .email("test@test.pl")
                .build();
    }
}
