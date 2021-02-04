package com.phalder.cocktailapp.repository.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class CockTailItem (
    var name: String,
    var instruction: String,
    var category: String,
    var thumbnail: String,
    var alchoholic: String,
    var glass: String,
    var ingredients: List<String>,
    var measures: List<String>,
    var id: String
): Parcelable
