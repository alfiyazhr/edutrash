package com.example.edutrash.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.edutrash.R
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.io.File
import java.io.IOException
import android.provider.Settings
import androidx.activity.result.PickVisualMediaRequest
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.util.Log
import android.view.View
import com.example.edutrash.helper.ImageClassifierHelper
import com.example.edutrash.view.activity.ResultActivity




class ScanTrashFragment : Fragment(R.layout.fragment_scan_trash) {

    private lateinit var cameraButton: Button
    private lateinit var previewImageView: ImageView
    private lateinit var analyzeButton: Button
    private lateinit var progressIndicator: LinearProgressIndicator
    private  val REQUEST_PERMISSION_CODE = 1001
    private lateinit var imageClassifierHelper: ImageClassifierHelper


    private var photoUri: Uri? = null



    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraButton = view.findViewById(R.id.button_camera)
        analyzeButton = view.findViewById(R.id.button_image_analyze)
        previewImageView = view.findViewById(R.id.previewImageView)
        progressIndicator = view.findViewById(R.id.progressIndicator)





        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    progressIndicator.visibility = View.GONE
                    showError(error)
                }

                override fun onResults(results: List<String>, inferenceTime: Long) {
                    progressIndicator.visibility = View.GONE
                    showResults(results)
                }
            }
        )
        cameraButton.setOnClickListener {
            showCameraOrGalleryDialog()
        }

        analyzeButton.setOnClickListener {
            analyzeImage()
        }

    }


    // Show a dialog to choose between camera and gallery
    private fun showCameraOrGalleryDialog() {
        val options = arrayOf("Open Camera", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Select Option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    // Open Camera Intent
    private fun openCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }

        photoFile?.also {
            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                it
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            cameraResultLauncher.launch(intent)
        }
    }


    // Open Gallery Intent
    private val selectPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                previewImageView.setImageURI(uri)
            }
        }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Create a PickVisualMediaRequest for image-only media
            val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            selectPhotoLauncher.launch(request)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryResultLauncher.launch(intent)
        }
    }






    // Handle Camera Result
    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoUri?.let {
                    setPreviewImage(it)
                }
            }
        }



    // Handle Gallery Result
    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    photoUri = uri // Tambahkan ini untuk memperbarui photoUri
                    setPreviewImage(uri)
                }
            }
        }



    // Create a temporary image file for camera capture
    private fun createImageFile(): File {
        val storageDir: File? = requireContext().getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            storageDir
        ).also {
            Log.d("ScanTrashFragment", "Image file created at: ${it.absolutePath}")
        }
    }

    private fun setPreviewImage(uri: Uri) {
        Log.d("ScanTrashFragment", "Setting preview image: $uri")
        val resizedBitmap = resizeBitmap(uri)
        if (resizedBitmap != null) {
            previewImageView.setImageBitmap(resizedBitmap)
            Log.d("ScanTrashFragment", "Image resized and set to preview")
        } else {
            previewImageView.setImageURI(uri)
            Log.d("ScanTrashFragment", "Fallback to setting URI directly")
        }
    }

    private fun resizeBitmap(uri: Uri): Bitmap? {
        return try {
            // Membaca gambar dari URI
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Mendapatkan lebar dan tinggi ImageView
            val targetWidth = previewImageView.width
            val targetHeight = previewImageView.height

            // Crop gambar untuk mempertahankan aspek rasio ImageView
            val croppedBitmap = cropToAspectRatio(originalBitmap, targetWidth, targetHeight)

            // Resize gambar hasil cropping ke dimensi ImageView
            Bitmap.createScaledBitmap(croppedBitmap, targetWidth, targetHeight, false)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun cropToAspectRatio(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val bitmapRatio = bitmap.width.toFloat() / bitmap.height
        val targetRatio = targetWidth.toFloat() / targetHeight

        return if (bitmapRatio > targetRatio) {
            // Gambar lebih lebar, crop sisi kanan dan kiri
            val newWidth = (bitmap.height * targetRatio).toInt()
            val xOffset = (bitmap.width - newWidth) / 2
            Bitmap.createBitmap(bitmap, xOffset, 0, newWidth, bitmap.height)
        } else {
            // Gambar lebih tinggi, crop bagian atas dan bawah
            val newHeight = (bitmap.width / targetRatio).toInt()
            val yOffset = (bitmap.height - newHeight) / 2
            Bitmap.createBitmap(bitmap, 0, yOffset, bitmap.width, newHeight)
        }
    }




    // Fungsi untuk memulai analisis gambar
    private fun analyzeImage() {
        val bitmap = photoUri?.let { uri -> correctBitmapOrientation(uri) }
        if (bitmap != null) {
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
            Log.d("ScanTrashFragment", "Bitmap analyzed: ${bitmap.hashCode()}")
            progressIndicator.visibility = View.VISIBLE
            imageClassifierHelper.classifyImage(resizedBitmap)

            // Reset photoUri untuk mencegah penggunaan gambar lama
            photoUri = null
        } else {
            showError(getString(R.string.no_image_selected))
        }
    }

    private fun showResults(results: List<String>) {
        // Temukan hasil dengan confidence score tertinggi
        val highestConfidenceResult = results.maxByOrNull { result ->
            val confidence = result.substringAfter(":").trim().removeSuffix("%").toFloatOrNull() ?: 0f
            confidence
        } ?: "Unknown"

        // Ambil jenis sampah dan confidence score dari hasil dengan confidence score tertinggi
        val wasteType = highestConfidenceResult.substringBefore(":").trim()
        val confidenceScore = highestConfidenceResult.substringAfter(":").trim()

        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra("RESULT_TEXT", "$wasteType: $confidenceScore")
            putExtra("RESULT_IMAGE_URI", photoUri.toString())
        }
        startActivity(intent)
    }


    // Menampilkan pesan error
    private fun showError(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }

    // Ekstensi untuk mengubah Drawable menjadi Bitmap
    private fun android.graphics.drawable.Drawable.toBitmapOrNull(): Bitmap? {
        return if (this is android.graphics.drawable.BitmapDrawable) {
            bitmap
        } else {
            null
        }
    }


    private fun correctBitmapOrientation(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val exif = ExifInterface(inputStream!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
            inputStream.close()

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
                else -> bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}





