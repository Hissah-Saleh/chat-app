package com.example.chatapp.domain.model

sealed class ChatItem {
    abstract val username: String
    abstract val time: String
    abstract val isMine: Boolean

    data class Text(
        override val username: String,
        override val time: String,
        val content: String,
        override val isMine: Boolean
    ) : ChatItem()

    data class Media(
        override val username: String,
        override val time: String,
        val url: String,
        val type: String,
//        val name: String,
        override val isMine: Boolean
    ) : ChatItem()
}
