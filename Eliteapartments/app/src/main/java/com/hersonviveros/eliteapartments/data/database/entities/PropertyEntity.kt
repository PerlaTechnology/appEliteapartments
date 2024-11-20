package com.hersonviveros.eliteapartments.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "PropertyTable", indices = [Index(value = ["id"], unique = true)])
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val maxGuests: Int,
    val beds: Int,
    val bathrooms: Int,
    val title: String,
    val description: String,
    val photos: List<String>, // Rutas de las fotos
    val latitude: Double,
    val longitude: Double
)
