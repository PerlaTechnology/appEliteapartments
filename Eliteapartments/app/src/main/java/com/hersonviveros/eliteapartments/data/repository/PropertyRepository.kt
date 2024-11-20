package com.hersonviveros.eliteapartments.data.repository

import com.hersonviveros.eliteapartments.data.database.dao.PropertyDao
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PropertyRepository @Inject constructor(private val propertyDao: PropertyDao) {
    suspend fun insertProperty(property: PropertyEntity) {
        propertyDao.insert(property)
    }

    suspend fun getAllProperties(): List<PropertyEntity> {
        return propertyDao.getAllProperties()
    }
}