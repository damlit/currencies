package com.pocket.currencies.users.entity;

import lombok.Data;

@Data
public class UserDto {

    private long id;
    private String email;
    private UserRole userRole;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
    }
}
