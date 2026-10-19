package com.moraisluis.split.dto.user;

import java.util.UUID;

import com.moraisluis.split.model.User;

public record UserResponse(UUID id, String name, String email) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
