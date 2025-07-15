package com.cp9v.lingua_learn_server.api.dto.requests;

import com.cp9v.lingua_learn_server.api.dto.WordPair;

import java.util.List;

public record CreateWordsRequest(List<WordPair> pairs) {
}
