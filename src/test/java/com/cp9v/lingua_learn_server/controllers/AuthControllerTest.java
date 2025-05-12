package com.cp9v.lingua_learn_server.controllers;

import com.cp9v.lingua_learn_server.BaseIT;
import com.cp9v.lingua_learn_server.api.dto.RegisterDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseIT {
    @Autowired
    public ObjectMapper objectMapper;

    @Test
    void register_WithInvalidEmail_ShouldReturn400() throws Exception {
        var dto = new RegisterDto(
                "infalid-email", "validPassword123", "John", "Doe"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_WithShortPassword_ShouldReturn400() throws Exception {
        var dto = new RegisterDto(
                "john@example.com", "1231", "John", "Doe"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_withBlancForm_shouldReturnAllFields() throws Exception {
        var request = new RegisterDto("","", "", "");
        var response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Map<String, String> res =  objectMapper.readValue(response, new TypeReference<>() {});
        assertThat(res).containsKeys("email", "password", "name", "surname");
    }

    @Test
    void register_shouldReturnError_ifUserExists() throws Exception {
        var request = objectMapper.writeValueAsString(
                new RegisterDto("john@example.com", "validPassword123", "John", "Doe")
        );
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((request)))
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"email\":\"User with this email already exists\"}"));
    }
}
