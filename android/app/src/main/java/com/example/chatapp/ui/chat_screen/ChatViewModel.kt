package com.example.chatapp.ui.chat_screen

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.domain.model.ChatItem
import com.example.chatapp.domain.use_case.ConnectToChatUseCase
import com.example.chatapp.domain.use_case.GetCurrentUsersNumberUseCase
import com.example.chatapp.domain.use_case.ObserveMassageUseCase
import com.example.chatapp.domain.use_case.ObserveNumberOfUsersUseCase
import com.example.chatapp.domain.use_case.ObserveServerUseCase
import com.example.chatapp.domain.use_case.RetriveUsernameUseCase
import com.example.chatapp.domain.use_case.SendMessageServerUseCase
import com.example.chatapp.domain.use_case.UploadMediaUseCase
import com.example.chatapp.util.Constants.STOP_TYPING
import com.example.chatapp.util.Constants.TYPING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getUsernameUseCase: RetriveUsernameUseCase,
    private val sendMessageServerUseCase: SendMessageServerUseCase,
    private val observeMassageUseCase: ObserveMassageUseCase,
    private val connectToChatUseCase: ConnectToChatUseCase,
    private val uploadMediaUseCase: UploadMediaUseCase,
    private val observeServerUseCase: ObserveServerUseCase,
    private val observeNumberOfUsersUseCase: ObserveNumberOfUsersUseCase,
    private val getCurrentUSersNumberUseCase: GetCurrentUsersNumberUseCase,

    ) : ViewModel() {

//    val chat = mutableStateListOf<ChatItem>()//<ChatMessage?>(null)
    private val _chatFlow = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatFlow: StateFlow<List<ChatItem>> = _chatFlow
    val scrollDown= mutableStateOf(false)
    var selectedUri = mutableStateOf<Uri?>(null)
    val showSelectedMediaPreview = mutableStateOf<Boolean>(false)
    val clickedImage = mutableStateOf<String?>(null)
    val clickedVideo = mutableStateOf<Uri?>(null)
    val numUsers = mutableIntStateOf(1)
    val loading = mutableStateOf<Boolean>(false)
    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    private var stopTypingJob: Job? = null

    init {
        observeNumberOfActiveUsers()
        observe()
    }


    fun userTyping() {
        sendMessageServerUseCase(TYPING,"")
        stopTypingJob?.cancel()
        stopTypingJob = viewModelScope.launch {
            delay(2000)
            sendMessageServerUseCase(STOP_TYPING,"")
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTypingJob?.cancel()
    }


    fun observe() {
        viewModelScope.launch {
            observeMassageUseCase() {
                _chatFlow.value = _chatFlow.value + it
            }
            observeServerUseCase(TYPING) { _isTyping.value = true }
            observeServerUseCase(STOP_TYPING) { _isTyping.value = false }
        }

    }

    fun setClickedImage(url: String?) {
        clickedImage.value = url
    }

    fun setClickedVideo(uri: Uri?) {
        clickedVideo.value = uri
    }

    fun sendMessage(text: String) {
        sendMessageServerUseCase(
            "message",
            text
        )
    }

    fun fetchVideoFrame(url: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(url, HashMap())
            retriever.getFrameAtTime(
                /*timeUs=*/1_000_000,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }

    fun getNumberOfUsers(){
        viewModelScope.launch{
            getCurrentUSersNumberUseCase({ numUsers.intValue=it })
        }
    }

    fun observeNumberOfActiveUsers() {
        observeNumberOfUsersUseCase(
            {
                numUsers.intValue = it
            }
        )
    }

    fun pickMedia(uri: Uri) {
        selectedUri.value = uri
        showSelectedMediaPreview.value = true
    }

    fun resetPickMedia() {
        selectedUri.value = null
        showSelectedMediaPreview.value = false
    }

    fun uploadMedia() {
        val uri = selectedUri.value ?: return
        viewModelScope.launch {
            loading.value = true
            uploadMediaUseCase(uri)
            resetPickMedia()
            loading.value = false

        }
    }
}