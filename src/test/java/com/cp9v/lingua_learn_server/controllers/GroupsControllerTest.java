package com.cp9v.lingua_learn_server.controllers;

import com.cp9v.lingua_learn_server.BaseIT;
import com.cp9v.lingua_learn_server.api.dto.Group;
import com.cp9v.lingua_learn_server.api.dto.GroupWithWords;
import com.cp9v.lingua_learn_server.api.dto.Word;
import com.cp9v.lingua_learn_server.api.dto.WordPair;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static com.cp9v.lingua_learn_server.api.dto.enums.Languages.EN;
import static com.cp9v.lingua_learn_server.api.dto.enums.Languages.RU;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "/sql/seed.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class GroupsControllerTest extends BaseIT {

    @Test
    public void testGetGroupsForUser() throws Exception {
        var request = get("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", "Bearer " + getTestToken());

        var response = mockMvc.perform(request)
                              .andExpect(status().isOk())
                              .andExpect(content().encoding("UTF-8"))
                              .andReturn().getResponse().getContentAsString();

        var groups = objectMapper.readValue(response, Group[].class);

        assertThat(groups).hasSize(2);
        assertThat(groups).containsExactlyInAnyOrder(
                new Group(101, "Eng to Rus", "Англо-русский", true, 3),
                new Group(301, "Czech to Eng", "Чешско-английский", false, 3)
        );
    }

    @Test
    public void testGetWordsForGroup() throws Exception {
        var request = get("/api/groups/101/words")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", "Bearer " + getTestToken());

        var response = mockMvc.perform(request)
                              .andExpect(status().isOk())
                              .andReturn().getResponse().getContentAsString();

        var groupWithWords = objectMapper.readValue(response, GroupWithWords.class);

        assertThat(groupWithWords.words()).hasSize(3);
        assertThat(groupWithWords.words()).containsExactlyInAnyOrder(
                new WordPair(101, new Word(101, "word_1", EN), new Word(201, "слово_1", RU)),
                new WordPair(102, new Word(102, "word_2", EN), new Word(202, "слово_2", RU)),
                new WordPair(103, new Word(103, "word_3", EN), new Word(203, "слово_3", RU))
        );
    }

    @Test
    public void testGetWordsForGroup_shouldThrowUnauthorized_ifGroupIsPrivate() throws Exception {
        var request = get("/api/groups/201/words")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", "Bearer " + getTestToken());

        mockMvc.perform(request)
               .andExpect(status().isUnauthorized());
    }

}
