package com.phalder.cocktailapp.repository.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "fav_cocktail_table")
data class CockTailItem (
    @PrimaryKey
    var id: String,
    var name: String,
    var instruction: String,
    var category: String,
    var thumbnail: String,
    var alchoholic: String,
    var glass: String,
    var ingredients: List<String>,
    var measures: List<String>

): Parcelable
