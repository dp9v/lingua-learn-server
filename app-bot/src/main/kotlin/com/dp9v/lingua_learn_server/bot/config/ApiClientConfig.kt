package com.dp9v.lingua_learn_server.bot.config

import com.dp9v.client.ApiClient
import com.dp9v.client.api.GroupsControllerApi
import com.dp9v.client.api.WordsControllerApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiClientConfig {

    @Bean
    fun apiClient(properties: ApiClientProperties): ApiClient {
        val client = ApiClient()
        client.basePath = properties.url
        client.addDefaultHeader("X-Bot-Api-Key", properties.apiKey)
        return client
    }

    @Bean
    fun wordsControllerApi(apiClient: ApiClient): WordsControllerApi {
        return WordsControllerApi(apiClient)
    }

    @Bean
    fun groupsControllerApi(apiClient: ApiClient): GroupsControllerApi {
        return GroupsControllerApi(apiClient)
    }
}
