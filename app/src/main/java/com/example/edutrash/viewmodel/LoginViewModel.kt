package com.example.edutrash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edutrash.api.RetrofitClient
import com.example.edutrash.api.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<Result<String>?>(null)
    val loginState : StateFlow<Result<String>?>get() = _loginState

    fun login(email: String, password: String){
        viewModelScope.launch {
            try{
                val response = userRepository.login(email, password)
                RetrofitClient.getInstance().setAuthToken(response.token)
                _loginState.value = Result.success(response.token)
            }catch(e: Exception){
                _loginState.value = Result.failure(e)
            }
        }
    }
}
