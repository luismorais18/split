package com.moraisluis.split.dto.split;

import com.moraisluis.split.dto.member.MemberResponse;
import com.moraisluis.split.model.ExpenseSplit;

public record SplitResponse(MemberResponse member, long amount) {
    public static SplitResponse from(ExpenseSplit split) {
        return new SplitResponse(MemberResponse.from(split.getUser()), split.getSplit());
    }
}