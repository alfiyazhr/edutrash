package com.example.edutrash.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edutrash.R
import com.example.edutrash.api.UserRepository
import com.example.edutrash.databinding.ActivitySignupBinding
import com.example.edutrash.viewmodel.SignUpViewModel
import com.example.edutrash.viewmodel.SignUpViewModelFactory

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signUpViewModel : SignUpViewModel by viewModels {
        SignUpViewModelFactory(UserRepository())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email : EditText = findViewById(R.id.etEmail)
        val username : EditText = findViewById(R.id.etUsername)
        val password : EditText = findViewById(R.id.etPassword)

        lifecycleScope.launchWhenStarted {
            signUpViewModel.registerState.collect{ result ->
                when{
                    result == null -> {
                        // Do Nothing
                    }
                    result.isSuccess -> {
                        val response = result.getOrNull()
                        Toast.makeText(this@SignUpActivity, response, Toast.LENGTH_SHORT).show()
                    }
                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        Toast.makeText(this@SignUpActivity, "Register Failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}
