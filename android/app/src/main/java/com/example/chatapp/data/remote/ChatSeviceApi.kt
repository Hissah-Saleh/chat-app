package com.example.chatapp.data.remote

import com.example.chatapp.data.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ChatSeviceApi {
    @Multipart
    @POST("upload")
    suspend fun uploadMedia(
        @Part media: MultipartBody.Part,
        @Part("username") username: RequestBody
    ): Response<UploadResponse>
}
