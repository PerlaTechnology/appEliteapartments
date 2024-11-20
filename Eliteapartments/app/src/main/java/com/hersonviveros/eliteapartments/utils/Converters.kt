package com.hersonviveros.eliteapartments.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // Convert the list of strings to a JSON string
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.let { Gson().toJson(it) }
    }

    // Convert a JSON string back to a list of strings
    @TypeConverter
    fun toList(data: String?): List<String>? {
        return data?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            Gson().fromJson<List<String>>(it, listType)
        }
    }
}
