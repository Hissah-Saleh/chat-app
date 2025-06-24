package com.example.chatapp.domain.use_case

import com.example.chatapp.data.repo.ChatRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddUserToChatUseCase @Inject constructor(
    private val repo: ChatRepo
) {

    operator fun invoke(
        userName: String
    ) {
        repo.send("add user", userName)
    }

}