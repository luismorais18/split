package com.moraisluis.split.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraisluis.split.dto.expense.ExpenseRequest;
import com.moraisluis.split.dto.expense.ExpenseResponse;
import com.moraisluis.split.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/expenses/" + created.id())).body(created);
    }

    @GetMapping
    public List<ExpenseResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public List<ExpenseResponse> findByGroupId(@PathVariable UUID id) {
        return service.findByGroupId(id);
    }

    
}
