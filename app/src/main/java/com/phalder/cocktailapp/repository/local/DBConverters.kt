package com.phalder.cocktailapp.repository.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DBConverters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun jsonfromList(value: List<String>): String {
            return Gson().toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun jsontoList(value: String): List<String> {
            val typeToken = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson<List<String>>(value, typeToken)

        }
    }
}