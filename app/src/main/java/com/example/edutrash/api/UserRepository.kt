package com.example.edutrash.api

import com.example.edutrash.model.LoginRequest
import com.example.edutrash.model.LoginResponse
import com.example.edutrash.model.RegisterRequest
import com.example.edutrash.model.RegisterResponse
import retrofit2.Call

class UserRepository{
    private val apiService = RetrofitClient.getInstance().apiService
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(email, password))
    }

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        return apiService.register(RegisterRequest(username,email,password))
    }
}