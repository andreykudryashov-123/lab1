package com.example.labone.data

enum class HealthStatus(val label: String) {
    SAFE("Безопасный"),
    WARNING("Содержит аллергены"),
    DANGEROUS("Вредный состав")
}

data class Product(
    val id: Int,
    val barcode: String,
    val name: String,
    val category: String,
    val ingredients: String,
    val calories: Int,
    val healthStatus: HealthStatus,
    val isFavorite: Boolean = false
)