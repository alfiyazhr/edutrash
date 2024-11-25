package com.example.edutrash.view.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.edutrash.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailTextView: TextView = findViewById(R.id.tvDetail)
        val detail = intent.getStringExtra("DETAIL") ?: "Detail not found"

        detailTextView.text = detail
    }
}
