package com.dicoding.asclepius.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.datasources.local.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : BaseActivity(){
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isPermissionGranted: Boolean ->
            if (isPermissionGranted) {
                displayToast("Permission request approved.")
            } else {
                displayToast("Permission request was not granted.")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        customizeActionBar()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.apply {
            analyzeButton.setOnClickListener{
                currentImageUri?.let {
                    analyzeImage(it)
                }?: displayToast("Image not selected")
            }
            galleryButton.setOnClickListener{
                initiateGallery()
            }
        }
    }

    private fun initiateGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { selectedUri: Uri? ->
        if (selectedUri != null) {
            val outputFileUri = Uri.fromFile(cacheDir.resolve("${System.currentTimeMillis()}.jpg"))
            UCrop.of(selectedUri, outputFileUri)
                .withMaxResultSize(2000, 2000)
                .start(this@MainActivity)
        } else {
            Log.d("Photo Picker", "Image not selected")
        }
    }

    @Deprecated(
        message = "Deprecated in Java",
        replaceWith = ReplaceWith(
            expression = "super.onActivityResult(requestCode, resultCode, data)",
            imports = ["androidx.appcompat.app.AppCompatActivity"]
        )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImagePreview()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("Crop Error", "onActivityResult: $cropError")
        }
    }

    private fun showImagePreview() {
        currentImageUri?.let { imageUri ->
            binding.previewImageView.setImageURI(imageUri)
            binding.analyzeButton.visibility = android.view.View.VISIBLE
            binding.galleryButton.apply {
                text = resources.getString(R.string.replace_image)
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.purple))
                background = ContextCompat.getDrawable(this@MainActivity, R.drawable.button_outline)
            }
        }
    }

    private fun analyzeImage(imageUri: Uri) {
        val imageProcessor = ImageClassifierHelper(
            context = this@MainActivity,
            listener = object : ImageClassifierHelper.ClassifierListener {
                override fun onFailure(error: String) {
                    displayToast(error)
                }

                override fun onSuccess(results: List<Classifications>?) {
                    val resultString = results?.joinToString("\n") { classification ->
                        val threshold = (classification.categories[0].score * 100).toInt()
                        "${classification.categories[0].label} : ${threshold}%"
                    }
                    resultString?.let { result ->
                        val historyData = HistoryEntity(
                            date = formatMillisecondsToDate(System.currentTimeMillis()),
                            uri = imageUri.toString(),
                            result = result
                        )
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.addHistory(historyData)
                            navigateToResult(imageUri, result)
                        }
                    }
                }
            }
        )
        imageProcessor.classifyStaticImage(imageUri)
    }

    private fun navigateToResult(imageUri: Uri, analysisResult: String) {
        val resultIntent = Intent(this@MainActivity, ResultActivity::class.java)
        resultIntent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
        resultIntent.putExtra(ResultActivity.EXTRA_RESULT, analysisResult)
        startActivity(resultIntent)
    }

    private fun displayToast(messageToShow: String) {
        Toast.makeText(this@MainActivity, messageToShow, Toast.LENGTH_SHORT).show()
    }

    fun formatMillisecondsToDate(millis: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd | HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return dateFormat.format(calendar.time)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}
