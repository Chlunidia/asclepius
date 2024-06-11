package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorSpace
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private val context: Context,
    private val listener: ClassifierListener?,
    private val thresholdValue: Float = DEFAULT_THRESHOLD,
    private val maxResultCount: Int = DEFAULT_MAX_RESULTS,
    private val numThreads: Int = DEFAULT_NUM_THREADS,
    private val modelFileName: String = MODEL_FILE_NAME
) {

    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(thresholdValue)
            .setMaxResults(maxResultCount)
            .setBaseOptions(BaseOptions.builder().setNumThreads(numThreads).build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelFileName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            listener?.onFailure(context.getString(R.string.classifier_failed))
            Log.e(TAG, "Error setting up image classifier: ${e.message}")
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val bitmap = loadImageBitmap(imageUri)
        val tensorImage = processImage(bitmap)
        val result = imageClassifier?.classify(tensorImage)
        listener?.onSuccess(result)
    }

    private fun loadImageBitmap(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.setTargetColorSpace(ColorSpace.get(ColorSpace.Named.SRGB))
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun processImage(bitmap: Bitmap): TensorImage {
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(IMAGE_SIZE, IMAGE_SIZE, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()

        return imageProcessor.process(TensorImage.fromBitmap(bitmap))
    }

    interface ClassifierListener {
        fun onFailure(error: String)
        fun onSuccess(results: List<Classifications>?)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
        private const val MODEL_FILE_NAME = "cancer_classification.tflite"
        private const val DEFAULT_THRESHOLD = 0.1f
        private const val DEFAULT_MAX_RESULTS = 3
        private const val DEFAULT_NUM_THREADS = 4
        private const val IMAGE_SIZE = 224
    }
}
