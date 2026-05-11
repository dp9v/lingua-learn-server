package com.dp9v.lingua_learn_server.bot.callback

data class PendingTranslation(
    val sourceText: String,
    val sourceLang: String,
    val translatedText: String,
    val targetLang: String,
)

