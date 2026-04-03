package com.cp9v.lingua_learn_server;

import com.cp9v.lingua_learn_server.api.components.JwtService;
import com.cp9v.lingua_learn_server.api.dto.UserDto;
import com.cp9v.lingua_learn_server.api.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "/sql/drop.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class BaseIT {
    public static UserDto TEST_USER_DTO = UserDto.builder()
                                                 .id(1001L)
                                                 .email("test@test.com")
                                                 .password("qwerty123")
                                                 .name("testName")
                                                 .surname("testSurname")
                                                 .build();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public JwtService jwtService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", TestDatabaseProvider::getJdbcUrl);
        registry.add("spring.datasource.username", TestDatabaseProvider::getUsername);
        registry.add("spring.datasource.password", TestDatabaseProvider::getPassword);
    }

    protected String getTestToken() {
        return jwtService.generateToken(User.builder()
                                            .id(TEST_USER_DTO.id())
                                            .email(TEST_USER_DTO.email())
                                            .build()).token();
    }
}
