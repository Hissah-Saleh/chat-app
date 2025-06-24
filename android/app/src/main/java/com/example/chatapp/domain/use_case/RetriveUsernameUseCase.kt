package com.example.chatapp.domain.use_case

import com.example.chatapp.data.repo.ChatRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetriveUsernameUseCase @Inject constructor(
    private val repo: ChatRepo
) {

    operator fun invoke(): Flow<String?> = repo.getUsername()

}