package com.example.labone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.labone.data.Product
import com.example.labone.data.HealthStatus
import com.example.labone.ui.screen.MainScreen
import com.example.labone.ui.screen.BarcodeScanScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("main") }
                    val context = LocalContext.current

                    // Проверка и запрос разрешения на камеру
                    var hasCameraPermission by remember {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    }

                    val cameraPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { granted ->
                            hasCameraPermission = granted
                            if (granted) {
                                currentScreen = "scanner"
                            }
                        }
                    )

                    val products = remember {
                        mutableStateOf(
                            listOf(
                                Product(
                                    id = 1,
                                    barcode = "460701095074",
                                    name = "Молоко 3.2%",
                                    category = "Молочные",
                                    ingredients = "Цельное коровье молоко",
                                    calories = 59,
                                    healthStatus = HealthStatus.SAFE,
                                    isFavorite = false
                                ),
                                Product(
                                    id = 2,
                                    barcode = "5449000000996",
                                    name = "Газировка Coca-Cola",
                                    category = "Напитки",
                                    ingredients = "Сахар, ортофосфорная кислота, кофеин",
                                    calories = 42,
                                    healthStatus = HealthStatus.DANGEROUS,
                                    isFavorite = true
                                )
                            )
                        )
                    }

                    when (currentScreen) {
                        "main" -> {
                            MainScreen(
                                products = products.value,
                                onToggleFavorite = { id: Int ->
                                    products.value = products.value.map { product ->
                                        if (product.id == id) product.copy(isFavorite = !product.isFavorite)
                                        else product
                                    }
                                },
                                onOpenScanner = {
                                    // Проверяем разрешение перед открытием сканера
                                    if (hasCameraPermission) {
                                        currentScreen = "scanner"
                                    } else {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            )
                        }
                        "scanner" -> {
                            if (hasCameraPermission) {
                                BarcodeScanScreen(
                                    onProductScanned = { barcode: String ->
                                        // Выводим уведомление с найденным штрихкодом
                                        Toast.makeText(context, "Найден штрихкод: $barcode", Toast.LENGTH_SHORT).show()

                                        // Возвращаемся на главный экран
                                        currentScreen = "main"
                                    },
                                    onBack = {
                                        currentScreen = "main"
                                    }
                                )
                            } else {
                                // Если разрешение отклонено, показываем кнопку запроса
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(onClick = {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }) {
                                        Text("Необходимо разрешение для доступа к камере")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}