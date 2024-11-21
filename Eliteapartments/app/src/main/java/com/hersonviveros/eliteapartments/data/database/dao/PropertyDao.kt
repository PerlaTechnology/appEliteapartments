package com.hersonviveros.eliteapartments.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity

@Dao
interface PropertyDao {

    @Query("SELECT * FROM PropertyTable")
    fun getAllProperties(): LiveData<List<PropertyEntity>>

    @Query("SELECT * FROM PropertyTable WHERE id = :id")
    suspend fun getById(id: String): PropertyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PropertyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: PropertyEntity)

    @Delete
    suspend fun delete(entity: PropertyEntity)


}