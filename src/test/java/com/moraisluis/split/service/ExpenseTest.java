package com.moraisluis.split.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.moraisluis.split.dto.expense.ExpenseRequest;
import com.moraisluis.split.dto.expense.ExpenseResponse;
import com.moraisluis.split.exception.ApiException;
import com.moraisluis.split.exception.ErrorCode;
import com.moraisluis.split.model.Expense;
import com.moraisluis.split.model.Group;
import com.moraisluis.split.model.User;
import com.moraisluis.split.repository.ExpenseRepository;
import com.moraisluis.split.repository.GroupRepository;
import com.moraisluis.split.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ExpenseTest {

    @Mock
    GroupRepository groupRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ExpenseRepository expenseRepository;

    @InjectMocks
    ExpenseService service;

    @Test
    void create_paidByNotInGroup_throwsNotAMember() {
        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        Group group = new Group("Viagem NYC");
        User user = new User("Ana", "ana@example.com");
        
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ExpenseRequest response = new ExpenseRequest("Descrição da despesa", groupId, userId, 123);

        ApiException ex = assertThrows(ApiException.class,
                () -> service.create(response));

        assertEquals(ErrorCode.NOT_A_MEMBER, ex.code());
        verify(expenseRepository, never()).save(any()); // nada foi gravado
    }

    @Test
    void create_paidByNotFound_throwsUserNotFound() {
        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ExpenseRequest response = new ExpenseRequest("Descrição da despesa", groupId, userId, 123);

        ApiException ex = assertThrows(ApiException.class,
                () -> service.create(response));

        assertEquals(ErrorCode.USER_NOT_FOUND, ex.code());
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void create_groupNotFound_throwsGroupNotFound() {
        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User user = new User("Ana", "ana@example.com");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        ExpenseRequest response = new ExpenseRequest("Descrição da despesa", groupId, userId, 123);

        ApiException ex = assertThrows(ApiException.class,
                () -> service.create(response));

        assertEquals(ErrorCode.GROUP_NOT_FOUND, ex.code());
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void create_withValidData_savesAndReturnsExpense() {
        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User firstUser = new User("Ana", "ana@example.com");
        User secondUser = new User("Joana", "joana@example.com");
        User thirdUser = new User("Jessica", "jessica@example.com");
        Group group = new Group("Viagem NYC");
        group.addMember(firstUser);
        group.addMember(secondUser);
        group.addMember(thirdUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(firstUser));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(inv -> inv.getArgument(0));

        ExpenseRequest response = new ExpenseRequest("Descrição da despesa", groupId, userId, 100);
        ExpenseResponse result = service.create(response);

        assertEquals("Descrição da despesa", result.description());

        assertEquals(34, result.splits().get(0).amount());
        assertEquals(33, result.splits().get(1).amount());
        assertEquals(33, result.splits().get(2).amount());

        verify(expenseRepository).save(any(Expense.class));
    }

    /**
     * @Test
    void create_withNewEmail_savesAndReturnsUser() {
        UserRequest request = new UserRequest("Ana", "ana@example.com");
        when(repository.existsByEmail("ana@example.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse response = service.create(request);

        assertEquals("Ana", response.name());
        assertEquals("ana@example.com", response.email());
        verify(repository).save(any(User.class));
    }
     */
}
