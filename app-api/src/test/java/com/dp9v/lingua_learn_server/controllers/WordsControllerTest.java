package com.dp9v.lingua_learn_server.controllers;

import com.dp9v.lingua_learn_server.BaseIT;
import com.dp9v.lingua_learn_server.api.dto.WordPairFull;
import com.dp9v.lingua_learn_server.api.dto.responses.GetRandomWordsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "/sql/seed.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class WordsControllerTest extends BaseIT {

    @Test
    public void testGetRandomWords_withValidParameters() throws Exception {
        var request = get("/api/words/random")
                .param("groups", "101", "301")
                .param("count", "5")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", "Bearer " + getTestToken());

        var response = mockMvc.perform(request)
                              .andExpect(status().isOk())
                              .andReturn().getResponse().getContentAsString();

        var randomWordsResponse = objectMapper.readValue(response, GetRandomWordsResponse.class);

        assertThat(randomWordsResponse).isNotNull();
        assertThat(randomWordsResponse.words()).size().isEqualTo(5);
        assertThat(randomWordsResponse.words())
                .allMatch(p -> Set.of(101L, 102L, 103L, 301L, 302L, 303L).contains(p.id()));
    }

    @Test
    public void testGetRandomWords_withSmallGroup() throws Exception {
        var request = get("/api/words/random")
                .param("groups", "101")
                .param("count", "5")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", "Bearer " + getTestToken());

        var response = mockMvc.perform(request)
                              .andExpect(status().isOk())
                              .andReturn().getResponse().getContentAsString();

        var randomWordsResponse = objectMapper.readValue(response, GetRandomWordsResponse.class);

        assertThat(randomWordsResponse).isNotNull();
        assertThat(randomWordsResponse.words()).size().isEqualTo(3);
        assertThat(randomWordsResponse.words().stream().map(WordPairFull::id))
                .containsExactlyInAnyOrder(101L, 102L, 103L);
    }

    @Test
    public void testGetRandomWords_withUnauthorizedGroup() throws Exception {
        var request = get("/api/words/random")
                .param("groups", "201")
                .param("count", "5")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", "Bearer " + getTestToken());

        mockMvc.perform(request)
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void testValidRequest() throws Exception {
        var request = get("/api/words/random")
                .param("groups", "201")
                .param("count", "31")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", "Bearer " + getTestToken());
        mockMvc.perform(request)
               .andExpect(status().isUnprocessableEntity());

        var groupsReq = IntStream.range(1, 30)
                                 .mapToObj(String::valueOf)
                                 .collect(Collectors.joining(","));
        request = get("/api/words/random")
                .param("groups", groupsReq)
                .param("count", "5")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authorization", "Bearer " + getTestToken());
        mockMvc.perform(request)
               .andExpect(status().isUnprocessableEntity());
    }
}

