package com.example.labone.viewmodel

import androidx.lifecycle.ViewModel
import com.example.labone.data.HealthStatus
import com.example.labone.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val initialProducts = listOf(
        Product(
            id = 1,
            barcode = "4601234567890",
            name = "Молоко 3.2%",
            category = "Молочные",
            ingredients = "Цельное молоко, обезжиренное молоко",
            calories = 60,
            healthStatus = HealthStatus.SAFE,
            isFavorite = true
        ),
        Product(
            id = 2,
            barcode = "4609876543210",
            name = "Газированный напиток Cola",
            category = "Напитки",
            ingredients = "Вода, сахар, краситель Е150d, ортофосфорная кислота, кофеин",
            calories = 42,
            healthStatus = HealthStatus.DANGEROUS,
            isFavorite = false
        ),
        Product(
            id = 3,
            barcode = "4605554433221",
            name = "Шоколад фундучный",
            category = "Сладости",
            ingredients = "Какао тертое, сахар, масло какао, дробленый фундук, лецитин, ароматизатор",
            calories = 540,
            healthStatus = HealthStatus.WARNING,
            isFavorite = false
        )
    )

    private val _products = MutableStateFlow<List<Product>>(initialProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(productId: Int) {
        _products.value = _products.value.map { product ->
            if (product.id == productId) product.copy(isFavorite = !product.isFavorite)
            else product
        }
    }

    fun addProduct(name: String, barcode: String, category: String, ingredients: String, calories: Int, status: HealthStatus) {
        val newProduct = Product(
            id = (_products.value.maxOfOrNull { it.id } ?: 0) + 1,
            barcode = barcode.ifBlank { "0000000000000" },
            name = name,
            category = category.ifBlank { "Общее" },
            ingredients = ingredients,
            calories = calories,
            healthStatus = status
        )
        _products.value = _products.value + newProduct
    }
}