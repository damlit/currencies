package com.pocket.currencies.registration;

import com.pocket.currencies.registration.entity.ConfirmationToken;
import com.pocket.currencies.registration.exception.EmailAlreadyConfirmedException;
import com.pocket.currencies.registration.exception.EmailIsNotValidException;
import com.pocket.currencies.registration.exception.TokenExpiredException;
import com.pocket.currencies.registration.exception.TokenIsNotValidException;
import com.pocket.currencies.users.entity.UserDto;
import com.pocket.currencies.users.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserServiceImpl userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;

    public String register(UserDto userDto) {
        boolean isValidEmail = emailValidator.test(userDto.getEmail());
        if (!isValidEmail) {
            throw new EmailIsNotValidException();
        }
        return userService.signUpUser(userDto);
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(TokenIsNotValidException::new);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getEmail());
        return "Token has been confirmed!";
    }
}
