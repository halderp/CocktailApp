package com.phalder.cocktailapp.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phalder.cocktailapp.R

class PhotoViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)

        // Get the Intent that started this activity and extract the string
        val stringURI = intent.getStringExtra(CameraCapture.EXTRA_PHOTO_VIEW_URI)

        // Update the UI View
        val imageView = findViewById<ImageView>(R.id.image_view).apply {
            Glide.with(this)
                .load(stringURI)
                .apply(RequestOptions())
                .into(this)
        }
    }
}