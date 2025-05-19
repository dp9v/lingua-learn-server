package com.cp9v.lingua_learn_server.flows;

import com.cp9v.lingua_learn_server.BaseIT;
import com.cp9v.lingua_learn_server.api.dto.UserDto;
import com.cp9v.lingua_learn_server.api.dto.auth.LoginResponse;
import com.cp9v.lingua_learn_server.api.dto.auth.MeResponse;
import com.cp9v.lingua_learn_server.api.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationFlowTest extends BaseIT {

    public static UserDto testUser;
    public static String testToken;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void testRegistration() throws Exception {
        testUser = UserDto.builder()
                          .email("test@test.com")
                          .password("12345678")
                          .name("testName")
                          .surname("testSurname")
                          .build();

        var request = post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser));

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
                .content(objectMapper.writeValueAsString(testUser));

        var response = mockMvc.perform(request)
                              .andExpect(status().is2xxSuccessful())
                              .andReturn()
                              .getResponse()
                              .getContentAsString();

        var loginResponse = objectMapper.readValue(response, LoginResponse.class);
        testToken = loginResponse.token();
        assertThat(loginResponse.token()).isNotNull();
    }

    @Test
    @Order(3)
    public void testAuthentication() throws Exception {
        var response = mockMvc
                .perform(get("/api/auth/me")
                                 .header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var meResponse = objectMapper.readValue(response, MeResponse.class);
        assertThat(meResponse.name()).isEqualTo("test@test.com");
    }
}
