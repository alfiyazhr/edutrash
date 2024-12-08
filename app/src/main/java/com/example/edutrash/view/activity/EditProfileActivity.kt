package com.example.edutrash.view.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.edutrash.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        // Set window insets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://capstone-7b47b-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        saveButton = findViewById(R.id.save)
        cancelButton = findViewById(R.id.cancel)

        // Handle Save button click
        saveButton.setOnClickListener {
            val newUsername = usernameEditText.text.toString().trim()
            val newPassword = passwordEditText.text.toString().trim()

            if (newUsername.isNotEmpty()) {
                updateUsername(newUsername)
            }
            if (newPassword.isNotEmpty()) {
                updatePassword(newPassword)
            }
        }

        // Handle Cancel button click
        cancelButton.setOnClickListener {
            // Close the activity without saving changes
            finish()
        }
    }

    private fun updateUsername(newUsername: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.child("users").child(userId)

            // Update the username in the database
            userRef.child("username").setValue(newUsername)
                .addOnSuccessListener {
                    Toast.makeText(this, "Username updated successfully!", Toast.LENGTH_SHORT).show()

                    // After the username is updated, finish the activity and return to the profile fragment
                    finish() // Return to the profile fragment or activity
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to update username: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePassword(newPassword: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Update the password in Firebase Authentication
            currentUser.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()

                        // After the password is updated, finish the activity and return to the profile fragment
                        finish() // Return to the profile fragment or activity
                    } else {
                        Toast.makeText(this, "Failed to update password: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
}
