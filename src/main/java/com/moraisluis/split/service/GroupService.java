package com.moraisluis.split.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.moraisluis.split.dto.group.GroupRequest;
import com.moraisluis.split.dto.group.GroupResponse;
import com.moraisluis.split.dto.group.GroupUpdateRequest;
import com.moraisluis.split.dto.member.AddMemberRequest;
import com.moraisluis.split.model.Group;
import com.moraisluis.split.model.User;
import com.moraisluis.split.repository.GroupRepository;
import com.moraisluis.split.repository.UserRepository;

@Service
public class GroupService {
    
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GroupResponse create(GroupRequest request) {
        Group group = new Group(request.name());

        Set<UUID> ids = request.memberIds() == null ? Set.of() : request.memberIds();
        List<User> users = userRepository.findAllById(ids);
        if (users.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more users not found");
        }
        users.forEach(group::addMember);
        return GroupResponse.from(groupRepository.save(group));
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> findAll() {
        return groupRepository.findAll().stream().map(GroupResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GroupResponse findById(UUID id) {
        return GroupResponse.from(getOrThrow(id));
    }

    @Transactional
    public GroupResponse update(UUID id, GroupUpdateRequest request) {
        Group group = getOrThrow(id);
        group.setName(request.name());
        return GroupResponse.from(group);
    }

    @Transactional
    public GroupResponse addMember(UUID id, AddMemberRequest request) {
        Group group = getOrThrow(id);
        User user = getUserOrThrow(request.userId());
        if (!group.addMember(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member");
        }
        return GroupResponse.from(group);
    }

    @Transactional
    public GroupResponse removeMember(UUID id, UUID memberId) {
        Group group = getOrThrow(id);
        User user = getUserOrThrow(memberId);
        if (!group.removeMember(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is not in the group");
        }
        return GroupResponse.from(group);
    }

    

    





    private Group getOrThrow(UUID id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
    }

    private User getUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
