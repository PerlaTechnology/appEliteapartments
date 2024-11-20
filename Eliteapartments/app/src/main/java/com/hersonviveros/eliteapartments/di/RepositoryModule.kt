package com.hersonviveros.eliteapartments.di

import com.hersonviveros.eliteapartments.data.database.dao.PropertyDao
import com.hersonviveros.eliteapartments.data.repository.PropertyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePropertyRepository(propertyDao: PropertyDao): PropertyRepository {
        return PropertyRepository(propertyDao)
    }
}