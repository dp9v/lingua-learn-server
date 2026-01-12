package com.cp9v.lingua_learn_server.controllers;

import com.cp9v.lingua_learn_server.BaseIT;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HealthControllerTest extends BaseIT {

    @Test
    void healthCheck_ShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/health"))
               .andExpect(status().isOk())
               .andExpect(content().string("OK"));
    }
}

