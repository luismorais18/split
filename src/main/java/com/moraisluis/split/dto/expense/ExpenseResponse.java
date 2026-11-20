package com.moraisluis.split.dto.expense;

import java.util.List;
import java.util.UUID;

import com.moraisluis.split.dto.member.MemberResponse;
import com.moraisluis.split.dto.split.SplitResponse;
import com.moraisluis.split.model.Expense;

public record ExpenseResponse(
        UUID id,
        UUID groupId,
        MemberResponse paidBy,
        long value,
        String description,
        String createdAt,
        List<SplitResponse> splits) {

    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getGroup().getId(),
                MemberResponse.from(expense.getPaidBy()),
                expense.getValue(),
                expense.getDescription(),
                expense.getCreatedAt() != null ? expense.getCreatedAt().toString() : null,
                expense.getSplits().stream().map(SplitResponse::from).toList());
    }
}