package com.example.mentalapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.time.Instant

@Entity
@JsonClass(generateAdapter = true)
data class Product(
    @PrimaryKey
    val productId:Long,
    val text:String,
    val lastModified:Instant
)
