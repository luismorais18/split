package com.moraisluis.split.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.moraisluis.split.dto.group.GroupRequest;
import com.moraisluis.split.dto.group.GroupUpdateRequest;
import com.moraisluis.split.dto.member.AddMemberRequest;
import com.moraisluis.split.model.Group;
import com.moraisluis.split.model.User;
import com.moraisluis.split.repository.GroupRepository;
import com.moraisluis.split.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    
    @Mock
    GroupRepository groupRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    GroupService service;

    @Test 
    void addMember_whenUserAlreadyMember_throwsConflict() {
        // Arrange
        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Group group = new Group("Viagem NYC");
        User user = new User("Ana", "ana@example.com");
        group.addMember(user); // já é membro

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act + Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.addMember(groupId, new AddMemberRequest(userId)));

        assertEquals(409, ex.getStatusCode().value());
    }

    @Test
    void removeMember_whenUserNotMember_throwsNotFound() {
        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Group group = new Group("Viagem NYC");
        User user = new User("Ana", "ana@example.com"); // existe, mas nunca foi adicionada

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.removeMember(groupId, userId));

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("User is not in the group", ex.getReason());
    }

    @Test
    void update_whenGroupDoesNotExist_throwsNotFound() {
        UUID groupId = UUID.randomUUID();
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.update(groupId, new GroupUpdateRequest("updated")));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void create_whenUserDoesNotExist_throwsNotFoundAndDoesNotSave() {   
        // Arrange: pedimos 1 utilizador que não existe
        Set<UUID> ids = Set.of(UUID.randomUUID());
        GroupRequest request = new GroupRequest("Viagem NYC", ids);

        when(userRepository.findAllById(ids)).thenReturn(List.of()); // não veio nenhum

        // Act + Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.create(request));

        assertEquals(404, ex.getStatusCode().value());
        verify(groupRepository, never()).save(any()); // nada foi gravado
    }
}
