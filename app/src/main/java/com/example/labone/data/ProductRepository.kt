package com.example.labone.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class HealthStatus {
    SAFE, ALLERGEN, DANGEROUS
}

data class Product(
    val id: Int = 0,
    val barcode: String,
    val name: String,
    val category: String,
    val ingredients: String,
    val calories: Int,
    val healthStatus: HealthStatus,
    val isFavorite: Boolean = false
)

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun getProductByBarcode(barcode: String): Product?
    suspend fun getProductById(id: Long): Product?
    suspend fun toggleFavorite(productId: Int)
    suspend fun addProduct(product: Product)
}

class ProductRepositoryImpl(private val productDao: ProductDao) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { entity ->
                Product(
                    id = entity.id.toInt(),
                    barcode = entity.barcode,
                    name = entity.name,
                    category = entity.category,
                    ingredients = entity.ingredients,
                    calories = entity.calories,
                    healthStatus = try {
                        HealthStatus.valueOf(entity.healthStatus)
                    } catch (_: Exception) {
                        HealthStatus.SAFE
                    },
                    isFavorite = entity.isFavorite
                )
            }
        }
    }

    override suspend fun getProductByBarcode(barcode: String): Product? {
        val entity = productDao.getProductByBarcode(barcode) ?: return null
        return Product(
            id = entity.id.toInt(),
            barcode = entity.barcode,
            name = entity.name,
            category = entity.category,
            ingredients = entity.ingredients,
            calories = entity.calories,
            healthStatus = try {
                HealthStatus.valueOf(entity.healthStatus)
            } catch (_: Exception) {
                HealthStatus.SAFE
            },
            isFavorite = entity.isFavorite
        )
    }

    override suspend fun getProductById(id: Long): Product? {
        val entity = productDao.getProductById(id) ?: return null
        return Product(
            id = entity.id.toInt(),
            barcode = entity.barcode,
            name = entity.name,
            category = entity.category,
            ingredients = entity.ingredients,
            calories = entity.calories,
            healthStatus = try {
                HealthStatus.valueOf(entity.healthStatus)
            } catch (_: Exception) {
                HealthStatus.SAFE
            },
            isFavorite = entity.isFavorite
        )
    }

    override suspend fun toggleFavorite(productId: Int) {
        val entity = productDao.getProductById(productId.toLong())
        if (entity != null) {
            val updatedEntity = entity.copy(isFavorite = !entity.isFavorite)
            productDao.insertProduct(updatedEntity)
        }
    }

    override suspend fun addProduct(product: Product) {
        val entity = ProductEntity(
            barcode = product.barcode,
            name = product.name,
            category = projectCategory(product), // или передавай поля напрямую
            ingredients = product.ingredients,
            calories = product.calories,
            healthStatus = product.healthStatus.name,
            isFavorite = product.isFavorite
        )
        productDao.insertProduct(entity)
    }

    // Вспомогательный метод, если в ProductEntity есть поле category
    private fun projectCategory(product: Product): String = product.category
}