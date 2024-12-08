package com.example.edutrash.api

import com.example.edutrash.model.LoginRequest
import com.example.edutrash.model.LoginResponse
import retrofit2.Call

class UserRepository{
    private val apiService = RetrofitClient.getInstance().apiService
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(email, password))
    }
}