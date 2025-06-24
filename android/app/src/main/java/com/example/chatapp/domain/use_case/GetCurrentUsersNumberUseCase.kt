package com.example.chatapp.domain.use_case

import com.example.chatapp.data.repo.ChatRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentUsersNumberUseCase @Inject constructor(
    private val repo: ChatRepo
) {

    operator fun invoke(
        getNumberOfUsers: (Int) -> Unit
     ) {
        repo.sendGetNumberOfUsers(getNumberOfUsers)
    }

}