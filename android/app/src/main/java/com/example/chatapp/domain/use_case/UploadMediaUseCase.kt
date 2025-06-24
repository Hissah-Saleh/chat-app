package com.example.chatapp.domain.use_case

import android.content.Context
import android.net.Uri
import com.example.chatapp.data.repo.ChatRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadMediaUseCase @Inject constructor(
    private val repo: ChatRepo,
    private val retriveUsernameUseCase: RetriveUsernameUseCase,
    @ApplicationContext private val context: Context
) {

    suspend operator fun invoke(uri:Uri) {
        val currentUser= retriveUsernameUseCase().first()
        repo.uploadMedia(uri, context,currentUser?:"" )
    }

}