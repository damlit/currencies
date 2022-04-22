package com.pocket.currencies.registration;

import com.pocket.currencies.registration.entity.ConfirmationToken;
import com.pocket.currencies.registration.exception.EmailAlreadyConfirmedException;
import com.pocket.currencies.registration.exception.EmailIsNotValidException;
import com.pocket.currencies.registration.exception.TokenExpiredException;
import com.pocket.currencies.registration.exception.TokenIsNotValidException;
import com.pocket.currencies.users.UserService;
import com.pocket.currencies.users.entity.UserRequestDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final Logger LOG = LoggerFactory.getLogger("logger");

    private final static String TOKEN_CONFIRMED_MSG = "Token has been confirmed!";

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;

    public String register(UserRequestDto userRequestDto) {
        LOG.info("Registration user (" + userRequestDto.getEmail() + ") is starting");
        boolean isValidEmail = emailValidator.test(userRequestDto.getEmail());
        if (!isValidEmail) {
            LOG.info("Email " + userRequestDto.getEmail() + " is not valid" );
            throw new EmailIsNotValidException();
        }
        LOG.info("Email " + userRequestDto.getEmail() + " is valid" );
        return userService.signUpUser(userRequestDto);
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(TokenIsNotValidException::new);

        if (confirmationToken.getConfirmedAt() != null) {
            LOG.info("Email was confirmed already");
            throw new EmailAlreadyConfirmedException();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            LOG.info("Token is expired");
            throw new TokenExpiredException();
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getEmail());
        LOG.info("Token (" + token + ") has been confirmed");
        return TOKEN_CONFIRMED_MSG;
    }
}
