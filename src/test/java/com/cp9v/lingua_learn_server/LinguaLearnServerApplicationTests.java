package com.cp9v.lingua_learn_server;

import com.cp9v.lingua_learn_server.api.repositories.WordsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LinguaLearnServerApplicationTests extends BaseIT {


    @Autowired
    WordsRepository wordsRepository;

    @Test
    void contextLoads() {
        var res = wordsRepository.getAll();
        assertThat(res).isEmpty();
    }

}
