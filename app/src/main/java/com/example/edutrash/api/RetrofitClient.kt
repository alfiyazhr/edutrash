package com.example.edutrash.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor

class RetrofitClient private constructor(){

    companion object {
        private const val  BASE_URL = "https://api.example.com"

        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            return instance ?: synchronized(this){
                instance ?: RetrofitClient().also { instance = it }
            }
        }

    }

    private var authToken : String? = null

    fun setAuthToken(token : String){
        authToken = token
    }

    private val interceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder().apply{
            authToken?.let{
                header("Authorization", "Bearer $it")
            }
        }.build()
        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
