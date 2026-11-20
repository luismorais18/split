package com.moraisluis.split.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moraisluis.split.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    
    @EntityGraph(attributePaths = "splits")
    List<Expense> findByGroupId(UUID id);
}