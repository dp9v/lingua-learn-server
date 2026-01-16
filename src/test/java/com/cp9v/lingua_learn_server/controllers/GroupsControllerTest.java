package com.cp9v.lingua_learn_server.controllers;

import com.cp9v.lingua_learn_server.BaseIT;
import com.cp9v.lingua_learn_server.api.dto.Group;
import com.cp9v.lingua_learn_server.api.dto.GroupWithWords;
import com.cp9v.lingua_learn_server.api.dto.Word;
import com.cp9v.lingua_learn_server.api.dto.WordPairFull;
import com.cp9v.lingua_learn_server.api.dto.requests.CreateWordsRequest;
import com.cp9v.lingua_learn_server.api.repositories.GroupsRepository;
import com.cp9v.lingua_learn_server.api.repositories.WordPairsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Comparator;
import java.util.List;

import static com.cp9v.lingua_learn_server.api.dto.enums.Languages.CZ;
import static com.cp9v.lingua_learn_server.api.dto.enums.Languages.EN;
import static com.cp9v.lingua_learn_server.api.dto.enums.Languages.RU;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "/sql/seed.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class GroupsControllerTest extends BaseIT {
    @Autowired
    GroupsRepository groupsRepository;
    @Autowired
    private WordPairsRepository wordPairsRepository;

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
                new Group(101L, "Eng to Rus", "English-Russian", true, 3),
                new Group(301L, "Czech to Eng", "Czech-English", false, 3)
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
                new WordPairFull(101L, new Word(101L, "word_1", EN), new Word(201L, "word_1_ru", RU)),
                new WordPairFull(102L, new Word(102L, "word_2", EN), new Word(202L, "word_2_ru", RU)),
                new WordPairFull(103L, new Word(103L, "word_3", EN), new Word(203L, "word_3_ru", RU))
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

    @Test
    public void testCrateGroup() throws Exception {
        var createGroupBody = new Group(null, "test_user_group", "test", true, 0);
        var request = post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGroupBody))
                .header("Authorization", "Bearer " + getTestToken());

        mockMvc.perform(request)
               .andExpect(status().isCreated());

        var userGroups = groupsRepository.getAllForUser(TEST_USER_DTO.id());
        var createdGroup = userGroups.stream()
                                     .min(Comparator.comparingLong(Group::id))
                                     .orElseThrow();

        assertThat(createdGroup)
                .matches(group -> !group.isPublic(), "Created group should be private")
                .matches(group -> group.name().equals("test_user_group"), "Created group should have correct name")
                .matches(group -> group.description().equals("test"), "Created group should have correct description");
    }

    @Test
    public void testAddWordsToGroup() throws Exception {
        var groupId = 301;
        var requestBody = new CreateWordsRequest(
                List.of(
                        new WordPairFull(null, new Word(null, "slovo_2", CZ), new Word(null, "word_2", EN)),
                        new WordPairFull(null, new Word(null, "slovo_4", CZ), new Word(null, "word_4", EN)),
                        new WordPairFull(null, new Word(null, "word_1", EN), new Word(null, "word_1_ru", RU))
                )
        );

        var request = put("/api/groups/%s/words".formatted(groupId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header("Authorization", "Bearer " + getTestToken());

        mockMvc.perform(request)
               .andExpect(status().isCreated());

        var pairs = wordPairsRepository.getPairsForGroup(groupId)
                                       .stream()
                                       .map(wpf -> new String[]{wpf.from().getText(), wpf.to().getText()})
                                       .toList();
        assertThat(pairs).containsExactlyInAnyOrder(
                new String[]{"slovo_1", "word_1"},
                new String[]{"slovo_2", "word_2"},
                new String[]{"slovo_3", "word_3"},
                new String[]{"slovo_4", "word_4"},
                new String[]{"word_1", "word_1_ru"});
    }

}
