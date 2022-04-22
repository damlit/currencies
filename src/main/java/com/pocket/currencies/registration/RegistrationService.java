package com.pocket.currencies.registration;

import com.pocket.currencies.users.entity.UserRequestDto;

public interface RegistrationService {

    String register(UserRequestDto userRequestDto);
    String confirmToken(String token);
}
