package com.example.edutrash.view.activity

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.edutrash.R

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultImage: ImageView = findViewById(R.id.result_image)
        val resultText: TextView = findViewById(R.id.result_text)

        // Ambil hasil dari Intent
        val resultTextData = intent.getStringExtra("RESULT_TEXT")
        val resultImageUri = intent.getStringExtra("RESULT_IMAGE_URI")

        // Set data ke UI
        resultText.text = resultTextData
        if (!resultImageUri.isNullOrEmpty()) {
            resultImage.setImageURI(Uri.parse(resultImageUri))
        }
    }
}
