package com.cp9v.lingua_learn_server.bot.client.clients;

import com.cp9v.lingua_learn_server.bot.client.dto.Update;
import com.cp9v.lingua_learn_server.bot.client.dto.UpdateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TgClient {
    private final static String GET_UPDATES_URL = "/getUpdates?offset=%s";

    private final RestTemplate tgRestTemplate;

    public List<Update> getUpdates(long offset) {
        var response = tgRestTemplate.getForEntity(GET_UPDATES_URL.formatted(offset), UpdateResponse.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("getUpdates failed with status " + response.getStatusCode());
        }
        return Objects.requireNonNull(response.getBody()).result;
    }
}
