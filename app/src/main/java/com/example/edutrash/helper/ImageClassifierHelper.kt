package com.example.edutrash.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.util.Log
import com.example.edutrash.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import com.example.edutrash.ml.GarbageClassificationModel

class ImageClassifierHelper(
    private val threshold: Float = 0.1f,
    private val maxResults: Int = 1,
    private val context: Context,
    private val classifierListener: ClassifierListener?
) {
    private val TAG = "ImageClassifierHelper"

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: List<String>, inferenceTime: Long)
    }

    fun classifyImage(bitmap: Bitmap) {
        try {
            // Inisialisasi model
            val model = GarbageClassificationModel.newInstance(context)

            // Pastikan bitmap sesuai dengan dimensi 150x150
            val resizedBitmap = createScaledBitmap(bitmap, 150, 150, true)

            // Muat bitmap ke TensorImage
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(resizedBitmap)

            // Input tensor buffer (1x150x150x3)
            val inputFeature0 = TensorBuffer.createFixedSize(
                intArrayOf(1, 150, 150, 3),
                DataType.FLOAT32
            )
            inputFeature0.loadBuffer(tensorImage.buffer)

            // Lakukan inferensi
            val startTime = System.currentTimeMillis()
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val inferenceTime = System.currentTimeMillis() - startTime

            // Ambil label dan skor
            val labels = context.resources.getStringArray(R.array.garbage_labels)
            val scores = outputFeature0.floatArray
            val results = labels.indices
                .filter { scores[it] >= threshold } // Terapkan threshold
                .sortedByDescending { scores[it] }  // Urutkan berdasarkan skor
                .take(maxResults)                   // Ambil hasil terbaik
                .map { labels[it] to (scores[it] * 100) } // Konversi skor ke persen

            model.close() // Tutup model setelah selesai

            // Kembalikan hasil melalui listener
            classifierListener?.onResults(
                results.map { "${it.first}: ${"%.2f".format(it.second)}%" },
                inferenceTime
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error during model inference: ${e.message}")
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
        }
    }
}
