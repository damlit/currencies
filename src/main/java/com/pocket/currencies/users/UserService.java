package com.pocket.currencies.users;

import com.pocket.currencies.users.entity.User;
import com.pocket.currencies.users.entity.UserDto;
import com.pocket.currencies.users.entity.UserRole;

public interface UserService {

    String signUpUser(UserDto user);
    int enableUser(String email);
    User getActiveUser();
    UserRole getActiveUserRole();
    boolean isAdminUser();
}
