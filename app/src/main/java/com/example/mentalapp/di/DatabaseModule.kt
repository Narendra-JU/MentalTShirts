package com.example.mentalapp.di

import android.content.Context
import androidx.room.Room
import com.example.mentalapp.R
import com.example.mentalapp.database.MentalDatabase
import com.example.mentalapp.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideMentalDatabase(@ApplicationContext context:Context):MentalDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            MentalDatabase::class.java,
            "${context.getString(R.string.app_name)}.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: MentalDatabase): ProductDao = db.productDao
}