package com.moraisluis.split.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.moraisluis.split.dto.expense.ExpenseRequest;
import com.moraisluis.split.dto.expense.ExpenseResponse;
import com.moraisluis.split.dto.group.GroupRequest;
import com.moraisluis.split.dto.group.GroupResponse;
import com.moraisluis.split.exception.ApiException;
import com.moraisluis.split.exception.ErrorCode;
import com.moraisluis.split.model.Expense;
import com.moraisluis.split.model.ExpenseSplit;
import com.moraisluis.split.model.Group;
import com.moraisluis.split.model.User;
import com.moraisluis.split.repository.ExpenseRepository;
import com.moraisluis.split.repository.GroupRepository;
import com.moraisluis.split.repository.UserRepository;

@Service
public class ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public ExpenseResponse create(ExpenseRequest request) {

        User user = userRepository.findById(request.paidById())
            .orElseThrow(() ->  new ApiException(ErrorCode.USER_NOT_FOUND));

        Group group = groupRepository.findById(request.groupId())
            .orElseThrow(() -> new ApiException(ErrorCode.GROUP_NOT_FOUND));
        
        Set<User> members = group.getMembers();

        if (!members.contains(user)) {
            throw new ApiException(ErrorCode.NOT_A_MEMBER);
        }
        Expense expense = new Expense(group, user, request.amount(), request.description(), new java.util.ArrayList<>());
    
        List<Long> splits = SplitCalculator.splitEqually(request.amount(), members.size());
        
        for(int i = 0; i < splits.size(); i++) {
            ExpenseSplit split = new ExpenseSplit();
            split.setUser((User) members.toArray()[i]);
            split.setSplit(splits.get(i));
            expense.addSplit(split);
        }

        return ExpenseResponse.from(expenseRepository.save(expense));
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> findAll() {
        return expenseRepository.findAll().stream().map(ExpenseResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> findByGroupId(UUID id) {
        return expenseRepository.findByGroupId(id).stream().map(ExpenseResponse::from).toList();
    }

}
