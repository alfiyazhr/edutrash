package com.example.edutrash.view.activity

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.edutrash.R
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultImage: ImageView = findViewById(R.id.result_image)
        val resultText: TextView = findViewById(R.id.result_text)
        val categoryTitle: TextView = findViewById(R.id.category_title)
        val categoryDescription: TextView = findViewById(R.id.category_description)

        // Ambil hasil dari Intent
        val resultTextData = intent.getStringExtra("RESULT_TEXT")
        val resultImageUri = intent.getStringExtra("RESULT_IMAGE_URI")

        // Ekstrak jenis sampah dari hasil (ambil sebelum ":" jika ada)
        val wasteType = resultTextData?.substringBefore(":")?.trim()?.lowercase(Locale.ROOT)

        // Set data ke UI
        resultText.text = resultTextData
        if (!resultImageUri.isNullOrEmpty()) {
            resultImage.setImageURI(Uri.parse(resultImageUri))
        }

        // Klasifikasi sampah
        val category = classifyWaste(wasteType)
        categoryTitle.text = category.first
        categoryDescription.text = category.second
    }

    private fun classifyWaste(wasteType: String?): Pair<String, String> {
        return when (wasteType) {
            "biological" -> {
                Pair("Organic", "Organic waste refers to biodegradable materials that come from plant or animal sources. These materials can decompose naturally through biological processes, making them environmentally friendly when properly managed.")
            }
            "brown-glass", "green-glass", "white-glass", "cardboard", "paper",
            "plastic", "metal", "clothes", "shoes" -> {
                Pair("Anorganic", "Anorganic waste includes non-biodegradable materials that do not originate from biological sources. These items take a long time to decompose or may not decompose at all, often contributing to environmental pollution if not recycled.")
            }
            "battery" -> {
                Pair("B3", "B3 waste (hazardous waste) contains substances that can be harmful to human health or the environment if improperly handled. These materials require special disposal methods to ensure safety and prevent contamination.")
            }
            else -> {
                Pair("Unknown", "Waste type not recognized.")
            }
        }
    }
}
