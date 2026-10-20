package com.moraisluis.split.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.moraisluis.split.dto.user.UserRequest;
import com.moraisluis.split.dto.user.UserResponse;
import com.moraisluis.split.model.User;
import com.moraisluis.split.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;  

    @InjectMocks
    UserService service;

    @Test
    void create_withNewEmail_savesAndReturnsUser() {
        UserRequest request = new UserRequest("Ana", "ana@example.com");
        when(repository.existsByEmail("ana@example.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse response = service.create(request);

        assertEquals("Ana", response.name());
        assertEquals("ana@example.com", response.email());
        verify(repository).save(any(User.class));
    }

    @Test
    void create_withExistingEmail_throwsConflictAndDoesNotSave() {
        UserRequest request = new UserRequest("Ana", "ana@example.com");
        when(repository.existsByEmail("ana@example.com")).thenReturn(true);

        ResponseStatusException ex =
                assertThrows(ResponseStatusException.class, () -> service.create(request));

        assertEquals(409, ex.getStatusCode().value());
        verify(repository, never()).save(any());
    }
}
