package com.dp9v.lingua_learn_server.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args)
}