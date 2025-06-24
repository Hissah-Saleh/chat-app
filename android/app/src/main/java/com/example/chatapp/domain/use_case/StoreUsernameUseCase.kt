package com.example.chatapp.domain.use_case

import com.example.chatapp.data.repo.ChatRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreUsernameUseCase @Inject constructor(
    private val repo: ChatRepo
) {

    suspend operator fun invoke(name: String) =
        repo.setUsername(name)

}