package com.pocket.currencies.users;

import com.pocket.currencies.users.entity.UserDto;
import com.pocket.currencies.users.entity.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/role")
    @ResponseStatus(HttpStatus.OK)
    public UserRole getUserRole() {
        return userService.getActiveUserRole();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser() {
        return userService.getActiveUserDto();
    }
}
