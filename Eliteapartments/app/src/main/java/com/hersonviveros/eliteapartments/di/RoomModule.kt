package com.hersonviveros.eliteapartments.di

import android.content.Context
import androidx.room.Room
import com.hersonviveros.eliteapartments.data.database.AppDatabase
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.getPropertyDao()
}