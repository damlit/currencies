package com.pocket.currencies.registration;

import com.pocket.currencies.users.entity.UserDto;

public interface RegistrationService {

    String register(UserDto userDto);
    String confirmToken(String token);
}
