package com.pocket.currencies.registration;

import com.pocket.currencies.registration.entity.ConfirmationToken;
import com.pocket.currencies.registration.repository.ConfirmationTokenRepository;
import com.pocket.currencies.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTest {

    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @BeforeEach
    public void setup() {
        confirmationTokenService = new ConfirmationTokenServiceImpl(confirmationTokenRepository);
    }

    @Test
    public void shouldSaveConfirmationToken() {
        ConfirmationToken token = new ConfirmationToken("test1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), new User());

        confirmationTokenService.saveConfirmationToken(token);

        verify(confirmationTokenRepository, times(1)).save(token);
    }

    @Test
    public void shouldGetConfirmationToken() {
        confirmationTokenService.getToken("test1");

        verify(confirmationTokenRepository, times(1)).findByToken("test1");
    }

    @Test
    public void shouldConfirmedConfirmationToken() {
        Clock clock = Clock.fixed(Instant.parse("2021-12-22T10:15:30.00Z"), ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);
        mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(dateTime);

        confirmationTokenService.setConfirmedAt("test1");

        verify(confirmationTokenRepository, times(1)).updateConfirmedAt("test1", LocalDateTime.now());

    }
}
