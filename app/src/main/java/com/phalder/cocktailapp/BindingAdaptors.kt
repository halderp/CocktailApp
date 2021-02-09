package com.phalder.cocktailapp

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.phalder.cocktailapp.utils.BaseRecyclerViewAdapter
import com.phalder.cocktailapp.utils.fadeIn
import com.phalder.cocktailapp.utils.fadeOut

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
                RequestOptions()
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.default_img))
            .into(cocktailImageView)
    }
}

@BindingAdapter("cocktailDetailImage")
fun bindCocktailDetailImage(cocktailImageView: ImageView, cocktailImgUrl: String?) {
    cocktailImgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(cocktailImageView.context)
            .load(imgUri)
            .apply(
                RequestOptions())
            .into(cocktailImageView)
    }
}

@BindingAdapter("fabIconImage")
fun bindCocktailDetailImage(fabIcon: FloatingActionButton, isItemInFavDB: Boolean) {
    if (isItemInFavDB)
        fabIcon.setImageResource(R.drawable.remove_icon)
    else
        fabIcon.setImageResource(R.drawable.save_icon)

}


@BindingAdapter("android:fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = true) {
    if (view.tag == null) {
        view.tag = true
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    } else {
        view.animate().cancel()
        if (visible == true) {
            if (view.visibility == View.GONE)
                view.fadeIn()
        } else {
            if (view.visibility == View.VISIBLE)
                view.fadeOut()
        }
    }
}

/**
 * Use binding adapter to set the recycler view data using livedata object
 */
@Suppress("UNCHECKED_CAST")
@BindingAdapter("android:liveData")
fun <T> setRecyclerViewData(recyclerView: RecyclerView, items: LiveData<List<T>>?) {
    items?.value?.let { itemList ->
        (recyclerView.adapter as? BaseRecyclerViewAdapter<T>)?.apply {
            clear()
            addData(itemList)
        }
    }
}