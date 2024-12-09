package com.example.edutrash.view.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.edutrash.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailImageView: ImageView = findViewById(R.id.ivDetailImage)
        val detailTitleTextView: TextView = findViewById(R.id.tvDetailTitle)
        val detailTextView: TextView = findViewById(R.id.tvDetail)

        val imageResId = intent.getIntExtra("IMAGE_RES_ID", -1)
        val title = intent.getStringExtra("TITLE") ?: "Title not found"
        val detail = intent.getStringExtra("DETAIL") ?: "Detail not found"

        if (imageResId != -1) {
            detailImageView.setImageResource(imageResId)
        }
        detailTitleTextView.text = title
        detailTextView.text = detail
    }
}
