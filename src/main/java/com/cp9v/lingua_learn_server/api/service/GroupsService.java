package com.cp9v.lingua_learn_server.api.service;

import com.cp9v.lingua_learn_server.api.dto.Group;
import com.cp9v.lingua_learn_server.api.exceptions.UnauthorisedException;
import com.cp9v.lingua_learn_server.api.repositories.GroupsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupsService {
    private final GroupsRepository groupsRepository;

    public void checkAvailability(long groupId, long userId) {
        groupsRepository.getByIdAndUserId(groupId, userId)
                        .orElseThrow(() -> new UnauthorisedException(
                                "Group id: %d is not available for you".formatted(groupId)
                        ));
    }

    public List<Group> getGroupsForUser(long userId) {
        return groupsRepository.getAllForUser(userId);
    }
}
