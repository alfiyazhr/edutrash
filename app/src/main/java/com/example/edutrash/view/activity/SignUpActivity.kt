package com.example.edutrash.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.edutrash.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.mindrot.jbcrypt.BCrypt
import com.google.firebase.FirebaseApp

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi Firebase secara eksplisit
        FirebaseApp.initializeApp(this)  // Menambahkan baris ini

        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val emailField: EditText = findViewById(R.id.etEmail)
        val usernameField: EditText = findViewById(R.id.etUsername)
        val passwordField: EditText = findViewById(R.id.etPassword)
        val signupButton: Button = findViewById(R.id.btnSignUp)

        signupButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                // Membuat hash password menggunakan bcrypt
                val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())

                // Mendaftar pengguna dengan Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val user = hashMapOf(
                                "email" to email,
                                "username" to username,
                                "password" to passwordHash,  // Menyimpan password yang sudah di-hash
                                "createdAt" to System.currentTimeMillis()
                            )

                            if (userId != null) {
                                // Menyimpan data pengguna (termasuk password yang sudah di-hash) ke Firebase Realtime Database
                                val database = FirebaseDatabase.getInstance("https://capstone-7b47b-default-rtdb.asia-southeast1.firebasedatabase.app").reference
                                database.child("users").child(userId).setValue(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
