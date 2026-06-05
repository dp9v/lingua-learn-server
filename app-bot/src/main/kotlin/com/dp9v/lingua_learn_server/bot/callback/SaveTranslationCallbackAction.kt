package com.dp9v.lingua_learn_server.bot.callback

enum class SaveTranslationCallbackAction(val actionName: String) {
    SAVE("SAVE_TRANSLATION"),
    CANCEL("CANCEL_TRANSLATION"),
    ;

    companion object {
        private val byActionName = entries.associateBy { it.actionName }

        fun fromActionName(actionName: String): SaveTranslationCallbackAction? = byActionName[actionName]
    }
}

