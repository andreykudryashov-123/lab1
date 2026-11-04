package com.example.labone.ui

import com.example.labone.data.HealthStatus
import com.example.labone.data.Product

sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(
        val products: List<Product>,
        val filteredProducts: List<Product>,
        val searchQuery: String = "",
        val selectedCategory: String = "Все",
        val categories: List<String> = listOf("Все", "Молочные", "Напитки", "Сладости"),
        val selectedProduct: Product? = null
    ) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

sealed interface ProductUiEvent {
    data class SearchQueryChanged(val query: String) : ProductUiEvent
    data class CategorySelected(val category: String) : ProductUiEvent
    data class ToggleFavorite(val productId: Int) : ProductUiEvent
    data class SelectProductByBarcode(val barcode: String) : ProductUiEvent
    data class AddProduct(
        val name: String,
        val barcode: String,
        val category: String,
        val ingredients: String,
        val calories: Int,
        val healthStatus: HealthStatus
    ) : ProductUiEvent
}