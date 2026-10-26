package com.moraisluis.split.dto.group;

import java.util.List;
import java.util.UUID;

import com.moraisluis.split.dto.member.MemberResponse;
import com.moraisluis.split.model.Group;

public record GroupResponse(UUID id, String name, List<MemberResponse> members) {
    public static GroupResponse from(Group group) {
        List<MemberResponse> members = group.getMembers().stream()
                .map(MemberResponse::from)
                .toList();
        return new GroupResponse(group.getId(), group.getName(), members);
    }
}