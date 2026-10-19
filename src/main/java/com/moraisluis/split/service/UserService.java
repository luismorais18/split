package com.moraisluis.split.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.moraisluis.split.dto.user.UserRequest;
import com.moraisluis.split.dto.user.UserResponse;
import com.moraisluis.split.model.User;
import com.moraisluis.split.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        User saved = repository.save(new User(request.name(), request.email()));
        return UserResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(UserResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return UserResponse.from(getOrThrow(id));
    }

    @Transactional
    public UserResponse update(UUID id, UserRequest request) {
        User user = getOrThrow(id);
        if (repository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        user.setName(request.name());
        user.setEmail(request.email());
        return UserResponse.from(user); // saved on commit (dirty checking)
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        repository.deleteById(id);
    }

    private User getOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
