package com.moraisluis.split.dto.group;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupRequest(
    @NotBlank @Size(max = 100) String name,
    Set<UUID> memberIds) {}

