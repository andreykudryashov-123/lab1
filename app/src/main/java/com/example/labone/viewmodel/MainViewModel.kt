package com.example.labone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labone.data.Product
import com.example.labone.data.ProductRepository
import com.example.labone.ui.ProductUiEvent
import com.example.labone.ui.ProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            repository.getProducts().collect { products ->
                val currentState = _uiState.value
                val searchQuery = if (currentState is ProductUiState.Success) currentState.searchQuery else ""
                val selectedCat = if (currentState is ProductUiState.Success) currentState.selectedCategory else "Все"

                _uiState.value = ProductUiState.Success(
                    products = products,
                    filteredProducts = filterProducts(products, searchQuery, selectedCat),
                    searchQuery = searchQuery,
                    selectedCategory = selectedCat
                )
            }
        }
    }

    fun onEvent(event: ProductUiEvent) {
        val state = _uiState.value as? ProductUiState.Success ?: return

        when (event) {
            is ProductUiEvent.SearchQueryChanged -> {
                val filtered = filterProducts(state.products, event.query, state.selectedCategory)
                _uiState.value = state.copy(searchQuery = event.query, filteredProducts = filtered)
            }
            is ProductUiEvent.CategorySelected -> {
                val filtered = filterProducts(state.products, state.searchQuery, event.category)
                _uiState.value = state.copy(selectedCategory = event.category, filteredProducts = filtered)
            }
            is ProductUiEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    repository.toggleFavorite(event.productId)
                }
            }
            is ProductUiEvent.SelectProductByBarcode -> {
                val product = state.products.find { it.barcode == event.barcode || it.id.toString() == event.barcode }
                _uiState.value = state.copy(selectedProduct = product)
            }
            is ProductUiEvent.AddProduct -> {
                viewModelScope.launch {
                    val newProduct = Product(
                        id = (state.products.maxOfOrNull { it.id } ?: 0) + 1,
                        barcode = event.barcode,
                        name = event.name,
                        category = event.category,
                        ingredients = event.ingredients,
                        calories = event.calories,
                        healthStatus = event.healthStatus
                    )
                    repository.addProduct(newProduct)
                }
            }
        }
    }

    private fun filterProducts(products: List<Product>, query: String, category: String): List<Product> {
        return products.filter { product ->
            val matchesCategory = category == "Все" || product.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isEmpty() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.barcode.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }
}