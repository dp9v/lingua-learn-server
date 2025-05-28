package com.cp9v.lingua_learn_server.api.service;

import com.cp9v.lingua_learn_server.api.dto.WordPair;
import com.cp9v.lingua_learn_server.api.repositories.WordPairsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordsService {
    private final WordPairsRepository wordPairsRepository;

    public List<WordPair> getPairsForGroup(long groupId) {
        return wordPairsRepository.getPairsForGroup(groupId);
    }
}
