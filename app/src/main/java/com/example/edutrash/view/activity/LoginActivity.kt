package com.example.edutrash.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.edutrash.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.mindrot.jbcrypt.BCrypt

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val usernameField: EditText = findViewById(R.id.etUsername)
        val passwordField: EditText = findViewById(R.id.etPassword)
        val loginButton: Button = findViewById(R.id.btnLogin)
        val signUpTextView: TextView = findViewById(R.id.tvSignUp)

        loginButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Mengambil data pengguna dari Realtime Database
                val database = FirebaseDatabase.getInstance("https://capstone-7b47b-default-rtdb.asia-southeast1.firebasedatabase.app").reference
                database.child("users")
                    .orderByChild("username")
                    .equalTo(username)
                    .get()
                    .addOnSuccessListener { dataSnapshot ->
                        if (dataSnapshot.exists()) {
                            val user = dataSnapshot.children.firstOrNull()
                            val storedPassword = user?.child("password")?.value.toString()

                            // Verifikasi password dengan bcrypt
                            if (BCrypt.checkpw(password, storedPassword)) {
                                // Login dengan FirebaseAuth menggunakan email dan password
                                val email = user?.child("email")?.value.toString()

                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            // Login berhasil, simpan sesi
                                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to query user: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        signUpTextView.setOnClickListener {
            // Navigate to SignUpActivity
            startActivity(Intent(this, SignUpActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)  // Optional: Add transition effect
        }
    }
}
