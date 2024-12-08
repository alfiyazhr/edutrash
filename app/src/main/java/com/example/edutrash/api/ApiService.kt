package com.example.edutrash.api

import com.example.edutrash.model.LoginRequest
import com.example.edutrash.model.LoginResponse
import com.example.edutrash.model.RegisterRequest
import com.example.edutrash.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register") // Ganti dengan endpoint registrasi API Anda
    fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("login") // Ganti dengan endpoint login API Anda
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}