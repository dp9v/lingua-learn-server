package com.cp9v.lingua_learn_server;

import com.cp9v.lingua_learn_server.api.dto.UserDto;
import com.cp9v.lingua_learn_server.api.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthControllerTest extends BaseIT {

    public UserDto user;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void testRegistration() throws Exception {
        user = UserDto.builder()
                .email("test@test.com")
                .password("123456")
                .name("testName")
                .surname("testSurname")
                .build();

        var request = post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail("test@test.com")
                .orElseThrow();
        assertThat(user)
                .matches(u -> u.getPassword().equals(user.getPassword()))
                .matches(u -> u.getName().equals(user.getName()))
                .matches(u -> u.getSurname().equals(user.getSurname()))
                .matches(u -> u.getEmail().equals(user.getEmail()));
    }

    @Test
    @Order(2)
    public void testLogin() throws Exception {
        var request = post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        var token = mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(token).isNotNull();
    }
}
