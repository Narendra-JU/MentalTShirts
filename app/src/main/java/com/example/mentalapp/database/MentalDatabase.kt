package com.example.mentalapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mentalapp.database.converter.InstantConverter
import com.example.mentalapp.database.dao.ProductDao
import com.example.mentalapp.database.entity.Product

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        Product::class
    ]
)
@TypeConverters(
    InstantConverter::class
)
abstract class MentalDatabase:RoomDatabase() {
    abstract val productDao:ProductDao
}