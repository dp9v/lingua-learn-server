package com.dp9v.lingua_learn_server.bot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "lingua-learn-api")
data class ApiClientProperties(
    val url: String,
    val apiKey: String
) {
    init {
        require(url.isNotBlank()) { "API url must not be blank" }
        require(apiKey.isNotBlank()) { "API key must not be blank" }
    }
}

