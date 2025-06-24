package com.example.chatapp.domain.use_case

import com.example.chatapp.data.repo.ChatRepo
import io.socket.emitter.Emitter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveServerUseCase @Inject constructor(
    private val repo: ChatRepo//: ChatSocketClient
) {

    operator fun invoke(
        event: String,
        listener: (Array<Any>) -> Unit
    ): Emitter? {
        return repo.observe(event, listener)
    }

}