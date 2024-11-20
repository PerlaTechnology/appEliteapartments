package com.hersonviveros.eliteapartments.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hersonviveros.eliteapartments.data.database.dao.PropertyDao
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.utils.Converters

@Database(entities = [PropertyEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPropertyDao(): PropertyDao
}