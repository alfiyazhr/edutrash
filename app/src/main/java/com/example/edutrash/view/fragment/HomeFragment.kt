package com.example.edutrash.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.edutrash.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://capstone-7b47b-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        // Reference to UI elements
        val tvWelcome: TextView = view.findViewById(R.id.tvWelcome)

        // Get the current user
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // If user is logged in, get their username from Firebase
            database.child("users")
                .child(currentUser.uid) // Get user by unique UID
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val username = dataSnapshot.child("username").value.toString()
                        tvWelcome.text = "Welcome back, $username"
                    } else {
                        tvWelcome.text = "Welcome back, User" // Default message if username is not found
                    }
                }
                .addOnFailureListener {
                    tvWelcome.text = "Failed to load username"
                }
        } else {
            // If user is not logged in, display a generic message
            tvWelcome.text = "Welcome back, Guest"
        }
    }
}
