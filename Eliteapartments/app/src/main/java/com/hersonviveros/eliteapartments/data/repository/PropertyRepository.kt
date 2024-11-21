package com.hersonviveros.eliteapartments.data.repository

import androidx.lifecycle.LiveData
import com.hersonviveros.eliteapartments.data.database.dao.PropertyDao
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PropertyRepository @Inject constructor(private val propertyDao: PropertyDao) {

    suspend fun insertProperty(property: PropertyEntity) {
        propertyDao.insert(property)
    }

    suspend fun updateProperties(property: PropertyEntity) {
        return propertyDao.update(property)
    }

    suspend fun deleteProperties(property: PropertyEntity) {
        return propertyDao.delete(property)
    }

    fun getAllProperties(): LiveData<List<PropertyEntity>> {
        return propertyDao.getAllProperties()
    }
}