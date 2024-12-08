package com.example.edutrash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edutrash.api.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _registerState = MutableStateFlow<Result<String>?>(null)
    val registerState : StateFlow<Result<String>?> get() = _registerState
    fun register(username: String, email: String, password: String){
        viewModelScope.launch {
            try{
                val response = userRepository.register(username,email,password)
                _registerState.value = Result.success(response.message)
            }catch (e: Exception){
                _registerState.value = Result.failure(e)
            }
        }
    }
}
