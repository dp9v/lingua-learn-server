package com.dp9v.lingua_learn_server.api.service;

import com.dp9v.lingua_learn_server.api.dto.Word;
import com.dp9v.lingua_learn_server.api.dto.WordPairFull;
import com.dp9v.lingua_learn_server.api.repositories.WordPairsRepository;
import com.dp9v.lingua_learn_server.api.repositories.WordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordsService {
    private final WordPairsRepository wordPairsRepository;
    private final WordsRepository wordsRepository;

    public List<WordPairFull> getPairsForGroup(long groupId) {
        return wordPairsRepository.getPairsForGroup(groupId);
    }

    public List<Word> createOnlyNewWords(List<WordPairFull> pairs) {
        HashSet<Word> uniqueWords = new HashSet<>();
        for (var pair : pairs) {
            uniqueWords.add(pair.from());
            uniqueWords.add(pair.to());
        }
        wordsRepository.saveAllNewWords(uniqueWords);
        return wordsRepository
                .findAllByTextAndLanguage(uniqueWords);
    }

    public List<WordPairFull> getRandomPairs(List<Long> groupIds, int count) {
        return wordPairsRepository.getRandomPairs(groupIds, count);
    }
}
