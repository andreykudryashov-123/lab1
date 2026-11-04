package com.example.labone.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.labone.data.Product
import com.example.labone.data.HealthStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    products: List<Product>,
    onToggleFavorite: (Int) -> Unit,
    onOpenScanner: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Сканер продуктов (ЛР3)") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenScanner,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Открыть сканер штрихкодов"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (products.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Список продуктов пуст", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products) { product ->
                        ProductItemCard(
                            product = product,
                            onToggleFavorite = { onToggleFavorite(product.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemCard(
    product: Product,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Избранное",
                        tint = if (product.isFavorite) Color.Red else Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Штрихкод: ${product.barcode}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Состав: ${product.ingredients}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            val statusColor = when (product.healthStatus) {
                HealthStatus.SAFE -> Color(0xFF2E7D32)
                HealthStatus.ALLERGEN -> Color(0xFFEF6C00)
                HealthStatus.DANGEROUS -> Color(0xFFC62828)
            }
            Text(
                text = "Статус: ${product.healthStatus.name}",
                color = statusColor,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}