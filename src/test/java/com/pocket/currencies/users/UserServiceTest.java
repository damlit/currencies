package com.pocket.currencies.users;

import com.pocket.currencies.pocket.repository.PocketRepository;
import com.pocket.currencies.registration.ConfirmationTokenService;
import com.pocket.currencies.users.entity.User;
import com.pocket.currencies.users.entity.UserDto;
import com.pocket.currencies.users.entity.UserRole;
import com.pocket.currencies.users.exception.EmailAlreadyExistsException;
import com.pocket.currencies.users.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
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
        UserDto userDto = new UserDto("example@test.pl", "123");
        User user = User.builder().email("example@test.pl").userRole(UserRole.USER).locked(false).enabled(false).build();
        when(userRepository.findFirstByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        userService.signUpUser(userDto);

        verify(confirmationTokenService, times(1)).saveConfirmationToken(any());
    }

    @Test
    public void shouldThrowExceptionForSignUpExistingUser() {
        UserDto userDto = new UserDto("example@test.pl", "123");
        User user = User.builder().email("example@test.pl").userRole(UserRole.USER).locked(false).enabled(true).build();
        when(userRepository.findFirstByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.signUpUser(userDto));
    }

    @Test
    public void shouldReturnTokenForNewUserUser() {
        UserDto userDto = new UserDto("example@test.pl", "123");
        when(userRepository.findFirstByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        userService.signUpUser(userDto);

        verify(confirmationTokenService, times(1)).saveConfirmationToken(any());
    }

    @Test
    public void shouldEnableUser() {
        String email = "example@test.pl";

        userService.enableUser("example@test.pl");

        verify(userRepository, times(1)).enableAppUser(email);
    }
}
