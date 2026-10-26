package com.moraisluis.split.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.moraisluis.split.dto.group.GroupRequest;
import com.moraisluis.split.dto.group.GroupResponse;
import com.moraisluis.split.dto.group.GroupUpdateRequest;
import com.moraisluis.split.dto.member.AddMemberRequest;
import com.moraisluis.split.service.GroupService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GroupResponse> create(@Valid @RequestBody GroupRequest request) {
        GroupResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/groups/" + created.id())).body(created);
    }

    @GetMapping
    public List<GroupResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public GroupResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public GroupResponse update(@PathVariable UUID id, @Valid @RequestBody GroupUpdateRequest request) {
        return service.update(id, request);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<GroupResponse> addMember(@PathVariable UUID id, @Valid @RequestBody AddMemberRequest request) {
        GroupResponse added = service.addMember(id, request);
        return ResponseEntity.created(URI.create("/groups/" + id + "/members/" + request.userId())).body(added);
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable UUID id, @PathVariable UUID memberId) {
        service.removeMember(id, memberId);
    }   
}
