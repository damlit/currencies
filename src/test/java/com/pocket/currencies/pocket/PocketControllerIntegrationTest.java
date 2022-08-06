package com.pocket.currencies.pocket;

import com.pocket.currencies.currencies.entity.Currency;
import com.pocket.currencies.currencies.entity.ExchangeQuote;
import com.pocket.currencies.currencies.entity.Quote;
import com.pocket.currencies.currencies.repository.ExchangeQuoteRepository;
import com.pocket.currencies.currencies.repository.QuoteRepository;
import com.pocket.currencies.mock.MockUtils;
import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Pocket;
import com.pocket.currencies.pocket.repository.DepositRepository;
import com.pocket.currencies.pocket.repository.PocketRepository;
import com.pocket.currencies.users.UserServiceImpl;
import com.pocket.currencies.users.entity.User;
import com.pocket.currencies.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PocketControllerIntegrationTest {

    private static final String POCKET_ENDPOINT = "/api/v1/pocket";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PocketRepository pocketRepository;
    @Autowired
    private DepositRepository depositRepository;
    @Autowired
    private ExchangeQuoteRepository exchangeQuoteRepository;
    @Autowired
    private QuoteRepository quoteRepository;
    @MockBean
    private UserServiceImpl userService;

    @Test
    @WithMockUser(username = "test@test.pl")
    public void testAddDeposit() throws Exception {
        Pocket pocket = new Pocket();
        User user = User.builder().email("test@test.pl").pocket(pocket).build();
        pocketRepository.save(pocket);
        userRepository.save(user);
        when(userService.getActiveUser()).thenReturn(user);
        String expectedResponse = "Success deposit with id 3";
        String requestBody = "{\"soldCurrency\": \"PLN\"," +
                "\"boughtCurrency\": \"USD\"," +
                "\"quote\": 5.0," +
                "\"soldSum\": 10000}";

        this.mvc.perform(post(POCKET_ENDPOINT + "/deposit/add")
                .content(requestBody)
                .header("Content-Type", "application/json")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
        Optional<Deposit> deposit = depositRepository.findById(3L);
        assertTrue(deposit.isPresent());
        assertEquals("2000.00", deposit.get().getBoughtSum().toPlainString());
        assertEquals("10000.00", deposit.get().getSoldSum().toPlainString());
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void testRemoveDeposit() throws Exception {
        String expectedResponse = "Deposit with id 3 has been removed";
        Pocket pocket = new Pocket();
        User user = User.builder().email("test@test.pl").pocket(pocket).build();
        pocketRepository.save(pocket);
        userRepository.save(user);
        when(userService.getActiveUser()).thenReturn(user);
        Deposit deposit = Deposit.builder().id(3L).build();
        depositRepository.save(deposit);

        assertEquals(1, depositRepository.count());
        this.mvc.perform(get(POCKET_ENDPOINT + "/deposit/remove/3")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
        assertEquals(0, depositRepository.count());
    }

    @Test
    @WithMockUser(username = "test@test.pl")
    public void testCalculateProfit() throws Exception {
        String expectedResponse = "{\"profit\":-12.65,\"depositsProfits\":[" +
                "{\"depositId\":2,\"profit\":-9.65,\"soldSum\":10.00,\"soldCurrency\":\"PLN\",\"boughtCurrency\":\"EUR\"}," +
                "{\"depositId\":3,\"profit\":-3.00,\"soldSum\":10.00,\"soldCurrency\":\"PLN\",\"boughtCurrency\":\"USD\"}]}";
        Pocket pocket = new Pocket();
        pocketRepository.save(pocket);
        Deposit deposit = MockUtils.createMockDeposit(Currency.EUR, BigDecimal.TEN, pocket);
        depositRepository.save(deposit);
        Deposit deposit1 = MockUtils.createMockDeposit(Currency.USD, BigDecimal.TEN, pocket);
        depositRepository.save(deposit1);
        User user = User.builder().email("test@test.pl").pocket(pocket).build();
        userRepository.save(user);
        when(userService.getActiveUser()).thenReturn(user);
        ExchangeQuote exchangeQuote = ExchangeQuote.builder().quotesDate(Date.valueOf(LocalDate.now())).source("USD").build();
        exchangeQuoteRepository.save(exchangeQuote);
        Quote quote = Quote.builder().quote(BigDecimal.valueOf(20L)).exchangeQuote(exchangeQuote).currency(Currency.EUR).build();
        Quote quote1 = Quote.builder().quote(BigDecimal.valueOf(7L)).exchangeQuote(exchangeQuote).currency(Currency.PLN).build();
        Quote quote2 = Quote.builder().quote(BigDecimal.ONE).exchangeQuote(exchangeQuote).currency(Currency.USD).build();
        quoteRepository.save(quote);
        quoteRepository.save(quote1);
        quoteRepository.save(quote2);

        assertEquals(2, depositRepository.count());
        assertEquals(3, quoteRepository.count());
        this.mvc.perform(get(POCKET_ENDPOINT + "/profit")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

    }
// todo check that / caches for profits / dockercompose
    @Test
    @WithMockUser(username = "test@test.pl")
    public void testGetDeposits() throws Exception {
        String expectedSoldSum = "1111";
        String expectedSoldSum1 = "2222";
        String expectedSoldSum2 = "3333";
        Pocket pocket = new Pocket();
        pocketRepository.save(pocket);
        Deposit deposit = Deposit.builder().pocket(pocket).soldSum(BigDecimal.valueOf(1111)).build();
        depositRepository.save(deposit);
        Deposit deposit1 = Deposit.builder().pocket(pocket).soldSum(BigDecimal.valueOf(3333)).build();
        depositRepository.save(deposit1);
        Deposit deposit2 = Deposit.builder().pocket(pocket).soldSum(BigDecimal.valueOf(2222)).build();
        depositRepository.save(deposit2);
        User user = User.builder().email("test@test.pl").pocket(pocket).build();
        userRepository.save(user);
        when(userService.getActiveUser()).thenReturn(user);

        assertEquals(3, depositRepository.count());
        this.mvc.perform(get(POCKET_ENDPOINT + "/deposit?page=0&size=1")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedSoldSum)));

        this.mvc.perform(get(POCKET_ENDPOINT + "/deposit?page=2&size=1")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedSoldSum1)));

        this.mvc.perform(get(POCKET_ENDPOINT + "/deposit?page=0&size=2")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedSoldSum2)));
    }
}
