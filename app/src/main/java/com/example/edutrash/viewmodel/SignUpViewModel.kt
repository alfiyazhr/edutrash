package com.example.edutrash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    var email: String = ""
    var username: String = ""
    var password: String = ""

    fun signUp() {
        viewModelScope.launch {
            // Tambahkan logika sign up di sini
            // Misalnya, validasi input, panggilan jaringan, dll.
            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                // Proses sign up berhasil
            } else {
                // Tampilkan pesan kesalahan
            }
        }
    }
}
