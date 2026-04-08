package com.dp9v.lingua_learn_server.api.service;

import com.dp9v.lingua_learn_server.api.dto.Group;
import com.dp9v.lingua_learn_server.api.dto.Word;
import com.dp9v.lingua_learn_server.api.dto.WordPair;
import com.dp9v.lingua_learn_server.api.dto.WordPairFull;
import com.dp9v.lingua_learn_server.api.exceptions.UnauthorisedException;
import com.dp9v.lingua_learn_server.api.repositories.GroupsRepository;
import com.dp9v.lingua_learn_server.api.repositories.WordPairsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final WordPairsRepository wordPairsRepository;
    private final WordsService wordsService;

    public Group getGroup(long groupId, long userId) {
        return groupsRepository.getByIdAndUserId(groupId, userId)
                               .orElseThrow(() -> new UnauthorisedException(
                                       "Group id: %d is not available for you".formatted(groupId)
                               ));
    }

    public List<Group> getGroupsForUser(long userId) {
        return groupsRepository.getAllForUser(userId);
    }

    public Long createGroup(Group group) {
        return groupsRepository.createGroup(group);
    }

    public void addPairs(long groupId, long userId, List<WordPairFull> pairs) {
        getGroup(groupId, userId);
        var wordsMapping = wordsService.createOnlyNewWords(pairs)
                                       .stream()
                                       .collect(Collectors.toMap(Function.identity(), Word::getId));

        var pairsToCreate = pairs.stream()
                                 .map(p -> new long[]{
                                         wordsMapping.get(p.from()),
                                         wordsMapping.get(p.to())})
                                 .toList();

        wordPairsRepository.createNewPairs(pairsToCreate);

        var pairIds = wordPairsRepository.findAllPairs(pairsToCreate)
                                         .stream()
                                         .map(WordPair::id)
                                         .toList();

        groupsRepository.addNewPairs(groupId, pairIds);
    }

    public void validateAccess(Set<Long> groups, long userId) {
        var userGroups = groupsRepository.getAllForUser(userId)
                .stream()
                .map(Group::id)
                .collect(Collectors.toSet());
        groups.removeAll(userGroups);
        if (!groups.isEmpty()) {
            throw new UnauthorisedException("You don't have access to some of the requested groups: " + groups);
        }
    }
}
