package com.hersonviveros.eliteapartments.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "PropertyTable", indices = [Index(value = ["id"], unique = true)])
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "property_type") val propertyType: String,
    @ColumnInfo(name = "max_guests") val maxGuests: Int,
    @ColumnInfo(name = "beds") val beds: Int,
    @ColumnInfo(name = "bathrooms") val bathrooms: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "photos") var photos: List<String>,
    @ColumnInfo(name = "location") var location: List<Position>
) : Serializable


data class Position(
    var latitud: Double,
    var longitud: Double
) : Serializable