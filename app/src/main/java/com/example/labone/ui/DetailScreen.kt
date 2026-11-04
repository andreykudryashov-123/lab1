package com.example.labone.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.labone.data.HealthStatus
import com.example.labone.data.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(product: Product, onBackClick: () -> Unit) {
    val (statusColor, statusText) = when (product.healthStatus) {
        HealthStatus.SAFE -> Color(0xFF4CAF50) to "Безопасно для употребления"
        HealthStatus.ALLERGEN -> Color(0xFFFF9800) to "Внимание: Содержит аллергены!"
        HealthStatus.DANGEROUS -> Color(0xFFF44336) to "Опасно: Вредные добавки!"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали продукта") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = product.name, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Категория: ${product.category}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Штрихкод: ${product.barcode}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Энерг. ценность: ${product.calories} ккал / 100g", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Полный состав:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = product.ingredients, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(statusColor.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}