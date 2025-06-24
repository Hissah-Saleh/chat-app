package com.example.chatapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.domain.use_case.AddUserToChatUseCase
import com.example.chatapp.domain.use_case.ConnectToChatUseCase
import com.example.chatapp.domain.use_case.LeftChatUseCase
import com.example.chatapp.domain.use_case.ObserveServerUseCase
import com.example.chatapp.domain.use_case.RetriveUsernameUseCase
import com.example.chatapp.util.Constants.LOGIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getUsernameUseCase: RetriveUsernameUseCase,
    private val connect: ConnectToChatUseCase,
    private val addUserToChat: AddUserToChatUseCase,
    private val observeServerUseCase: ObserveServerUseCase,
    private val leftChatUseCase: LeftChatUseCase,
) : ViewModel() {

    val username = mutableStateOf<String?>(null)
    val loading = mutableStateOf<Boolean>(true)

    init {
        getUsername()
    }

    private fun getUsername() {
        viewModelScope.launch {
            getUsernameUseCase().collect { text ->
                username.value = text

                if (text?.isNotEmpty() == true) {
                    loading.value = true
                    addUserToChat( text)
                    observeServerUseCase(LOGIN) {
                        loading.value = false

                    }

                } else {
                    loading.value = false
                }
            }
        }
    }

    fun fireOnResume(){
        connect()
        if(username.value?.isNotEmpty() == true)
            addUserToChat(username.value.orEmpty())

    }

    fun disconnectSocket() {
        leftChatUseCase()
    }

    override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }

}