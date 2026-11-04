package com.example.labone.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        database.productDao().insertAll(
                            listOf(
                                ProductEntity(
                                    barcode = "4607010950074",
                                    name = "Молоко 3.2%",
                                    category = "Молочные",
                                    ingredients = "Цельное коровье молоко",
                                    calories = 60,
                                    healthStatus = "SAFE",
                                    isFavorite = true
                                ),
                                ProductEntity(
                                    barcode = "5449000000996",
                                    name = "Газировка Coca-Cola",
                                    category = "Напитки",
                                    ingredients = "Сахар, ортофосфорная кислота, кофеин",
                                    calories = 42,
                                    healthStatus = "DANGEROUS",
                                    isFavorite = false
                                ),
                                ProductEntity(
                                    barcode = "7622210287178",
                                    name = "Шоколад Молочный",
                                    category = "Сладости",
                                    ingredients = "Какао-масло, сахар, сухое молоко",
                                    calories = 530,
                                    healthStatus = "ALLERGEN",
                                    isFavorite = true
                                ),
                                ProductEntity(
                                    barcode = "4606009383023",
                                    name = "Творог 5%",
                                    category = "Молочные",
                                    ingredients = "Нормализованное молоко, закваска",
                                    calories = 120,
                                    healthStatus = "SAFE",
                                    isFavorite = false
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}