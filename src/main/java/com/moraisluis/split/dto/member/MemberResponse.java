package com.moraisluis.split.dto.member;

import java.util.UUID;
    
import com.moraisluis.split.model.User;

public record MemberResponse(UUID id, String name) {
    public static MemberResponse from(User user) {
        return new MemberResponse(user.getId(), user.getName());
    }
}
