package com.hersonviveros.eliteapartments.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hersonviveros.eliteapartments.data.database.entities.Position

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
            Gson().fromJson(it, listType)
        }
    }

    @TypeConverter
    fun fromLocationList(locationList: List<Position>?): String? {
        if (locationList == null) return null
        val gson = Gson()
        return gson.toJson(locationList) // Convierte la lista a un String JSON
    }

    @TypeConverter
    fun toLocationList(locationString: String?): List<Position>? {
        if (locationString == null) return null
        val gson = Gson()
        val listType = object : TypeToken<List<Position>>() {}.type
        return gson.fromJson(
            locationString,
            listType
        ) // Convierte el JSON de nuevo a una lista de Position
    }
}
