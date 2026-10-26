package com.moraisluis.split.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moraisluis.split.model.Group;

public interface GroupRepository extends JpaRepository<Group, UUID> {

    @Override
    @EntityGraph(attributePaths = "members")
    List<Group> findAll();

}
