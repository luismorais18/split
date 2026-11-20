package com.moraisluis.split.dto.expense;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ExpenseRequest(
    @NotBlank @Size(max = 100) String description,
    @NotNull UUID groupId,
    @NotNull UUID paidById,
    @NotNull @Positive long amount
) {}

