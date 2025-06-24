package com.example.chatapp.data.repo

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.chatapp.data.local.UserSharedPreferences
import com.example.chatapp.data.remote.ChatSeviceApi
import com.example.chatapp.data.remote.ChatSocketClient
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepo @Inject constructor(
    private val userSharedPreferences: UserSharedPreferences,
    private val chatSocketClient: ChatSocketClient,
    private val chatSeviceApi: ChatSeviceApi
) {

    fun getUsername(): Flow<String?> = userSharedPreferences.getCurrentUser()

    suspend fun setUsername(name: String) = userSharedPreferences.setUsername(name)

    suspend fun uploadMedia(
        uri: Uri,
        context: Context,
        username: String
    ): Result<Unit> {
        // copy Uri → temp file
        val resolver = context.contentResolver
        val m = resolver.getType(uri) //?: return
        val ext = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(m)
            ?.let { ".$it" }
            ?: ""
// now create the temp file with that suffix
        val tmp = File.createTempFile("upload", ext, context.cacheDir)
// copy the content

        resolver.openInputStream(uri)?.use { input ->
            tmp.outputStream().use { output -> input.copyTo(output) }
        } ?: return Result.failure(Exception("Cannot read file"))

        // build MultipartBody.Part
        val mime = resolver.getType(uri) ?: "application/octet-stream"
        val body = tmp.asRequestBody(mime.toMediaTypeOrNull())
        val mediaPart = MultipartBody.Part.createFormData("media", tmp.name, body)

        // build username RequestBody
        val userPart = username.toRequestBody("text/plain".toMediaType())

        // call upload (no headers)
        val resp = chatSeviceApi.uploadMedia(mediaPart, userPart)
        return try {
            if (resp.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Upload failed: ${resp.code()}"))
        } finally {
            // delete the file after
            tmp.delete()
        }
    }

    fun connect() {
        chatSocketClient.connect()
    }

    fun disconnect() {
        chatSocketClient.disconnect()
    }

    fun send(event: String, args: String) {
        chatSocketClient.send(event= event,args= args)
    }

    fun sendGetNumberOfUsers(  getNumberOfUsers: (Int) -> Unit){
        chatSocketClient.sendEventWithAck("getNumUsers"){args->
            val count = (args.firstOrNull() as? Number)?.toInt() ?: 0
            getNumberOfUsers(count)
            Timber.d("Active users via ack: $count")
        }
    }

    fun observe(event: String, listener: (Array<Any>) -> Unit): Emitter? {
        return chatSocketClient.observe(event, listener)
    }



//    fun send(event: String, vararg args: Any) {
//        chatSocketClient.send(event, args)
//    }
}