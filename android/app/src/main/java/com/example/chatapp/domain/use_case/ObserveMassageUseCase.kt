package com.example.chatapp.domain.use_case

import com.example.chatapp.data.remote.NetworkConstants
import com.example.chatapp.data.repo.ChatRepo
import com.example.chatapp.domain.model.ChatItem
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMassageUseCase @Inject constructor(
    private val repo: ChatRepo,
    val retriveUsernameUseCase: RetriveUsernameUseCase
) {

    suspend operator fun invoke(
        listener: (ChatItem) -> Unit
    ){

        val currentUser= retriveUsernameUseCase().first()

        repo.observe("message"){
            val obj = it[0] as JSONObject
            val user = obj.getString("username")
            val time = obj.getString("time")
            val media = obj.optJSONObject("media")
            val isMine= user== currentUser

            if (media!=null) {
                listener(ChatItem.Media(
                    username = user,
                    time = time,
                    isMine = isMine,
                    url = "${NetworkConstants.CHAT_SERVER_URL}${media.getString("url")}",
                    type     = media.getString("type"),

                ))
            } else if (obj.has("text")) {
                listener(ChatItem.Text(
                    username = user,
                    time     = time,
                    content  = obj.getString("text"),
                    isMine = isMine

                ))
            }

        }

    }
}