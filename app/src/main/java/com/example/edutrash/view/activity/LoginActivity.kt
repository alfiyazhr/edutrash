package com.example.edutrash.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.edutrash.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val tvSignUp: TextView = findViewById(R.id.tvSignUp)

        btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvSignUp.setOnClickListener {
            // Navigate to SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
