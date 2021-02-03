package com.phalder.cocktailapp

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

// This is the binding adaptor for Displaying "Random Cocktail Image" using Glide.
// The default placeholder and error image is packaged in the app resources for better user experience
// First Type of Binding of ImageView

@BindingAdapter("cocktailImage")
fun bindCocktailImage(cocktailImageView: ImageView, cocktailImgUrl: String?) {
    cocktailImgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(cocktailImageView.context)
            .load(imgUri)
            .apply(
                RequestOptions())
            .into(cocktailImageView)
    }
}