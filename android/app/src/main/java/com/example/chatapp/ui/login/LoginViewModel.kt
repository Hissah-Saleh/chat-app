package com.example.chatapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.domain.use_case.StoreUsernameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val storeUsernameUseCase: StoreUsernameUseCase
): ViewModel(){


    fun addUser(username:String){
        viewModelScope.launch{
            storeUsernameUseCase(username)
        }
    }

}