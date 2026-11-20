package com.moraisluis.split.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraisluis.split.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsById(UUID id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
}