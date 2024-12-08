package com.example.edutrash.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edutrash.R
import com.example.edutrash.api.ApiService
import com.example.edutrash.api.RetrofitClient
import com.example.edutrash.api.UserRepository
import com.example.edutrash.model.LoginRequest
import com.example.edutrash.model.LoginResponse
import com.example.edutrash.viewmodel.LoginViewModel
import com.example.edutrash.viewmodel.LoginViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val loginViewModel : LoginViewModel by viewModels {
        LoginViewModelFactory(UserRepository())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val email: EditText = findViewById(R.id.etEmail)
        val password: EditText = findViewById(R.id.etPassword)
        val tvSignUp: TextView = findViewById(R.id.tvSignUp)

        // Observer login state
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginState.collect{ result ->
                when {
                    result == null -> {
                        // Do Nothing
                    }
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                    }
                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        Toast.makeText(this@LoginActivity, "Login Failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnLogin.setOnClickListener {
            // call loginViewModel
            loginViewModel.login(email.toString(), password.toString())
        }

        tvSignUp.setOnClickListener {
            // Navigate to SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
