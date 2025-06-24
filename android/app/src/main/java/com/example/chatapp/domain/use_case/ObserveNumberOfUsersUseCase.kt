package com.example.chatapp.domain.use_case

import com.example.chatapp.data.repo.ChatRepo
import io.socket.emitter.Emitter
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveNumberOfUsersUseCase @Inject constructor(
    private val repo: ChatRepo
) {

    operator fun invoke(
        onNumberRetreived: (Int) -> Unit
    ): Emitter? {
        return repo.observe("numUsers",   {arr->
            Timber.d(arr.size.toString())
            onNumberRetreived((arr.firstOrNull() as? Number)?.toInt() ?: 1)

        })
    }

}