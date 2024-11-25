package com.example.edutrash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var username: String = ""
    var password: String = ""

    fun login() {
        viewModelScope.launch {
            // Tambahkan logika login di sini
            // Misalnya, validasi input, panggilan jaringan, dll.
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Proses login berhasil
            } else {
                // Tampilkan pesan kesalahan
            }
        }
    }
}
