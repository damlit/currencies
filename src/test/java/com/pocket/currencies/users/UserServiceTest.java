package com.pocket.currencies.users;

import com.pocket.currencies.pocket.repository.PocketRepository;
import com.pocket.currencies.registration.ConfirmationTokenService;
import com.pocket.currencies.users.entity.User;
import com.pocket.currencies.users.entity.UserRequestDto;
import com.pocket.currencies.users.entity.UserRole;
import com.pocket.currencies.users.exception.EmailAlreadyExistsException;
import com.pocket.currencies.users.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PocketRepository pocketRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository, pocketRepository, bCryptPasswordEncoder, confirmationTokenService);
    }

    @Test
    public void shouldReturnTokenForExistingUser() {
        UserRequestDto userRequestDto = new UserRequestDto("example@test.pl", "123");
        User user = User.builder().email("example@test.pl").userRole(UserRole.USER).locked(false).enabled(false).build();
        when(userRepository.findFirstByEmail(userRequestDto.getEmail())).thenReturn(Optional.of(user));
        UUID uuid = UUID.randomUUID();
        MockedStatic<UUID> mockedSettings = mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);

        String token = userService.signUpUser(userRequestDto);

        verify(confirmationTokenService, times(1)).saveConfirmationToken(any());
        assertEquals(uuid.toString(), token);
        mockedSettings.close();
    }

    @Test
    public void shouldThrowExceptionForSignUpExistingUser() {
        UserRequestDto userRequestDto = new UserRequestDto("example@test.pl", "123");
        User user = User.builder().email("example@test.pl").userRole(UserRole.USER).locked(false).enabled(true).build();
        when(userRepository.findFirstByEmail(userRequestDto.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.signUpUser(userRequestDto));
    }

    @Test
    public void shouldReturnTokenForNewUserUser() {
        UserRequestDto userRequestDto = new UserRequestDto("example@test.pl", "123");
        when(userRepository.findFirstByEmail(userRequestDto.getEmail())).thenReturn(Optional.empty());
        UUID uuid = UUID.randomUUID();
        MockedStatic<UUID> mockedSettings = mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);

        String token = userService.signUpUser(userRequestDto);

        verify(confirmationTokenService, times(1)).saveConfirmationToken(any());
        assertEquals(uuid.toString(), token);
        mockedSettings.close();
    }

    @Test
    public void shouldEnableUser() {
        String email = "example@test.pl";

        userService.enableUser("example@test.pl");

        verify(userRepository, times(1)).enableAppUser(email);
    }
}
