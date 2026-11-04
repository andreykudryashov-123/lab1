package com.example.labone.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val barcode: String,
    val name: String,
    val category: String,
    val ingredients: String,
    val calories: Int,
    val healthStatus: String, // Сохраняем enum как строку ("SAFE", "ALLERGEN", "DANGEROUS")
    val isFavorite: Boolean = false
)