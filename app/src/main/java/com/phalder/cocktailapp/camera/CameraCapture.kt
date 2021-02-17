package com.phalder.cocktailapp.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phalder.cocktailapp.R
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraCapture : AppCompatActivity() {

    private var savedUri: String = ""
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewFinder: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_capture)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        if (savedInstanceState != null) {
            savedUri = savedInstanceState.getString(KEY_PHOTO_URI,"")
            if (!savedUri.isNullOrEmpty()){
                setPhotoButtonDisplayView(Uri.parse(savedUri))
                startMotionLayout()
            }
        }

        // Set up the listener for take photo button
        var camera_capture_button: Button = findViewById(R.id.camera_capture_button)
        camera_capture_button.setOnClickListener { takePhoto() }

        // Set up the listener for done photo button
        var done_photo_button: ImageButton = findViewById(R.id.photo_done_button)
        done_photo_button.setOnClickListener { doneTakingPhoto() }

        // Set up the listener for image view  button
        var image_view_button: ImageButton = findViewById(R.id.photo_view_button)
        image_view_button.setOnClickListener { openImageForDisplay() }

        viewFinder = findViewById(R.id.viewFinder)

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_PHOTO_URI, savedUri)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun openImageForDisplay() {
        if (!savedUri.isNullOrEmpty()) {
            val photoViewIntent = Intent(this, PhotoViewActivity::class.java).apply {
                putExtra(EXTRA_PHOTO_VIEW_URI, savedUri)
            }
            startActivity(photoViewIntent)
        }
    }


    private fun doneTakingPhoto() {

        if (savedUri.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            val result = Intent()
            result.putExtra(EXTRA_PHOTO_DESCRIPTION, savedUri)
            setResult(Activity.RESULT_OK, result)
            finish()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Timber.e("Photo capture failed: " + exc.message)
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile).toString()
                    // update the thumbnail image view
                    setPhotoButtonDisplayView(Uri.fromFile(photoFile))
                    startMotionLayout()

                   /* val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show() */

                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            // Preview
            val rotation = viewFinder.display.rotation

            val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
            val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)

            val preview = Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()
            preview.setSurfaceProvider(viewFinder.surfaceProvider)

            // ImageCapture
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                // We request aspect ratio but no resolution to match preview config, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Timber.e("Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    private fun setPhotoButtonDisplayView(uri: Uri) {
        val photoImageView = findViewById<ImageView>(R.id.photo_view_button)
        photoImageView.post {
            // Load thumbnail into circular button using Glide
            Glide.with(photoImageView)
                .load(uri)
                .apply(RequestOptions())
                .into(photoImageView)
        }
    }

    private fun startMotionLayout() {
        // Start Motion Layout Transition
        val mainMotionLayout = findViewById<MotionLayout>(R.id.mainMotionLayout)
        mainMotionLayout.transitionToEnd()

        val photoDoneButton = findViewById<ImageView>(R.id.photo_done_button)
        photoDoneButton.post {
            // Load thumbnail into circular button using Glide
            Glide.with(photoDoneButton)
                .load(R.drawable.ic_save_button)
                .apply(RequestOptions())
                .into(photoDoneButton)
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        val EXTRA_PHOTO_DESCRIPTION = "photo"
        val EXTRA_PHOTO_VIEW_URI = "photoview"
        val KEY_PHOTO_URI = "photouri"
    }
}