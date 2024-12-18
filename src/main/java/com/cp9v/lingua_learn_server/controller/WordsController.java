package com.cp9v.lingua_learn_server.controller;

import com.cp9v.lingua_learn_server.dto.Word;
import com.cp9v.lingua_learn_server.repositories.WordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/words")
public class WordsController {

    private final WordsRepository wordsRepository;

    @GetMapping
    public List<Word> getAll() {
        return wordsRepository.getAll();
    }
}
