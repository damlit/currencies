package com.pocket.currencies.registration;

import com.pocket.currencies.pocket.entity.Pocket;
import com.pocket.currencies.pocket.repository.DepositRepository;
import com.pocket.currencies.pocket.repository.PocketRepository;
import com.pocket.currencies.registration.entity.ConfirmationToken;
import com.pocket.currencies.registration.repository.ConfirmationTokenRepository;
import com.pocket.currencies.users.entity.User;
import com.pocket.currencies.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegistrationControllerIntegrationTest {

    private static final String REGISTRATION_ENDPOINT = "/api/v1/registration";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private PocketRepository pocketRepository;
    @Autowired
    private DepositRepository depositRepository;

    @Test
    public void testRegister() throws Exception {
        UUID uuid = UUID.randomUUID();
        MockedStatic<UUID> mockedSettings = mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);
        String requestBody = "{\"email\":\"test@test.pl\", \"password\":\"abc\"}";
        this.mvc.perform(post(REGISTRATION_ENDPOINT)
                .content(requestBody)
                .header("Content-Type", "application/json")
                .accept(MediaType.ALL))
                .andExpect(status().isCreated())
                .andExpect(content().string(uuid.toString()));
        assertEquals(1, confirmationTokenRepository.count());
        mockedSettings.close();
    }

    @Test
    public void testToken() throws Exception {
        User user = User.builder().email("test@test.pl").build();
        userRepository.save(user);
        ConfirmationToken token = new ConfirmationToken(
                "1234",
                LocalDateTime.of(2021, 12, 23, 12, 0),
                LocalDateTime.of(3000, 12, 23, 12, 30),
                user);
        confirmationTokenRepository.save(token);

        this.mvc.perform(get(REGISTRATION_ENDPOINT + "/confirm?token=1234")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string("Token has been confirmed!"));
        User confirmedUser = userRepository.findByEmail("test@test.pl");
        Optional<ConfirmationToken> confirmedToken = confirmationTokenRepository.findByToken("1234");
        assertTrue(confirmedToken.isPresent());
        assertNotNull(confirmedToken.get().getConfirmedAt());
        assertTrue(confirmedUser.getEnabled());
    }
}
