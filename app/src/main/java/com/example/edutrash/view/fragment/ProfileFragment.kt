package com.example.edutrash.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.edutrash.R
import com.example.edutrash.view.activity.EditProfileActivity
import com.example.edutrash.view.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://capstone-7b47b-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        // Referensi ke elemen UI
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val btnEdit: Button = view.findViewById(R.id.btnAbout)

        // Mendapatkan data pengguna saat ini
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Pengguna sudah login, tampilkan data
            tvEmail.text = currentUser.email

            // Mencari username dari Firebase Realtime Database
            database.child("users")
                .child(currentUser.uid) // Mendapatkan pengguna berdasarkan UID yang unik
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val username = dataSnapshot.child("username").value.toString()
                        tvUsername.text = username
                    } else {
                        tvUsername.text = " User not found"
                    }
                }
                .addOnFailureListener {
                    tvUsername.text = "Failed to load username"
                    tvEmail.text = "Failed to load email"
                }
        } else {
            // Jika pengguna belum login, tampilkan status Guest
            tvUsername.text = "Guest"
            tvEmail.text = "No email"
        }

        // Fungsi Logout
        btnLogout.setOnClickListener {
            auth.signOut() // Menghapus sesi Firebase
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent) // Mengarahkan ke halaman login
            requireActivity().finish() // Menutup aktivitas saat ini
        }

        // Fungsi Edit Profile
        btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    // Untuk menangani data yang diperbarui setelah EditProfileActivity selesai
    override fun onResume() {
        super.onResume()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Dapatkan kembali data terbaru setelah kembali dari EditProfileActivity
            database.child("users")
                .child(currentUser.uid)
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val username = dataSnapshot.child("username").value.toString()
                        val tvUsername: TextView = requireView().findViewById(R.id.tvUsername)
                        tvUsername.text = username
                    }
                }
        }
    }
}
