package com.cp9v.lingua_learn_server.api.service;

import com.cp9v.lingua_learn_server.api.dto.Word;
import com.cp9v.lingua_learn_server.api.dto.WordPair;
import com.cp9v.lingua_learn_server.api.dto.WordPairFull;
import com.cp9v.lingua_learn_server.api.repositories.WordPairsRepository;
import com.cp9v.lingua_learn_server.api.repositories.WordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordsService {
    private final WordPairsRepository wordPairsRepository;
    private final WordsRepository wordsRepository;

    public List<WordPairFull> getPairsForGroup(long groupId) {
        return wordPairsRepository.getPairsForGroup(groupId);
    }


    public void createWordsIfNotExist(List<WordPairFull> pairs) {
        HashSet<Word> uniqueWords = new HashSet<>();
        for (var pair : pairs) {
            uniqueWords.add(pair.from());
            uniqueWords.add(pair.to());
        }
        wordsRepository.saveAllNewWords(uniqueWords);
        var wordsToId = wordsRepository
                .findAllByTextAndLanguage(uniqueWords)
                .stream()
                .collect(Collectors.toMap(Function.identity(), Word::getId));

        var newPairs = pairs.stream()
                            .map(p -> new WordPair(null, wordsToId.get(p.from()), wordsToId.get(p.to())))
                            .filter(p -> Objects.equals(p.fromId(), p.toId()))
                            .toList();
    }
}
