package com.example.mentalapp.database.dao

import androidx.room.*
import com.example.mentalapp.database.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(product: Product)

    @Query("SELECT * FROM Product")
    fun getProducts(): Flow<Product>

    @Delete
    fun deleteProduct(product: Product)

}