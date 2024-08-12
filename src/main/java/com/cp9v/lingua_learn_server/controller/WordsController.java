package com.cp9v.lingua_learn_server.controller;

import com.cp9v.lingua_learn_server.dto.Word;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/words")
public class WordsController {

    @GetMapping
    public List<Word> getAll() {
        return List.of(
            new Word(1, "test1", "test1_meaning"),
            new Word(2, "test2", "test2_meaning"),
            new Word(3, "test3", "test3_meaning")
        );
    }
}
