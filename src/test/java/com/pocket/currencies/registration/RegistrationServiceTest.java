package com.pocket.currencies.registration;

import com.pocket.currencies.registration.entity.ConfirmationToken;
import com.pocket.currencies.registration.exception.EmailAlreadyConfirmedException;
import com.pocket.currencies.registration.exception.EmailIsNotValidException;
import com.pocket.currencies.registration.exception.TokenExpiredException;
import com.pocket.currencies.registration.exception.TokenIsNotValidException;
import com.pocket.currencies.users.UserService;
import com.pocket.currencies.users.entity.User;
import com.pocket.currencies.users.entity.UserRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    private RegistrationService registrationService;
    @Mock
    private UserService userService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailValidator emailValidator;

    private MockedStatic<LocalDateTime> mockedStatic;

    @BeforeEach
    public void setup() {
        registrationService = new RegistrationServiceImpl(userService, confirmationTokenService, emailValidator);

        Clock clock = Clock.fixed(Instant.parse("2021-12-18T19:15:30.00Z"), ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);
        mockedStatic = mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(dateTime);
    }

    @AfterEach
    public void close() {
        mockedStatic.close();
    }

    @Test
    public void shouldRegisterUser() {
        UserRequestDto userRequestDto = new UserRequestDto("test@test.pl", "test123");
        when(emailValidator.test(userRequestDto.getEmail())).thenReturn(true);

        registrationService.register(userRequestDto);

        verify(userService, times(1)).signUpUser(userRequestDto);
    }

    @Test
    public void shouldThrowExceptionForNotValidationEmail() {
        UserRequestDto userRequestDto = new UserRequestDto("test.pl", "test123");
        when(emailValidator.test(userRequestDto.getEmail())).thenReturn(false);

        Assertions.assertThrows(EmailIsNotValidException.class,
        () -> registrationService.register(userRequestDto));
    }

    @Test
    public void shouldConfirmToken() {
        String expectedMessage = "Token has been confirmed!";
        User user = User.builder().email("test@test.pl").build();
        ConfirmationToken token = new ConfirmationToken("123",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                user);
        when(confirmationTokenService.getToken("123")).thenReturn(Optional.of(token));

        String message = registrationService.confirmToken("123");

        verify(confirmationTokenService, times(1)).setConfirmedAt("123");
        verify(userService, times(1)).enableUser("test@test.pl");
        assertEquals(expectedMessage, message);
    }

    @Test
    public void shouldNotConfirmTokenForIncorrectToken() {
        when(confirmationTokenService.getToken("123")).thenReturn(Optional.empty());

        Assertions.assertThrows(TokenIsNotValidException.class,
                () -> registrationService.confirmToken("123"));
    }

    @Test
    public void shouldThrowExceptionForConfirmedEmail() {
        ConfirmationToken token = new ConfirmationToken();
        token.setToken("123");
        token.setConfirmedAt(LocalDateTime.now());
        when(confirmationTokenService.getToken("123")).thenReturn(Optional.of(token));

        Assertions.assertThrows(EmailAlreadyConfirmedException.class,
                () -> registrationService.confirmToken("123"));
    }

    @Test
    public void shouldThrowExceptionForExpiredToken() {
        User user = User.builder().email("test@test.pl").build();
        ConfirmationToken token = new ConfirmationToken("123",
                LocalDateTime.now().minusMinutes(16),
                LocalDateTime.now().minusMinutes(1),
                user);
        when(confirmationTokenService.getToken("123")).thenReturn(Optional.of(token));

        Assertions.assertThrows(TokenExpiredException.class,
                () -> registrationService.confirmToken("123"));
    }
}
