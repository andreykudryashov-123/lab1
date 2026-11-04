package com.example.labone.di

import com.example.labone.data.AppDatabase
import com.example.labone.data.ProductRepository
import com.example.labone.data.ProductRepositoryImpl
import com.example.labone.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // 1. Создаем синглтон базы данных Room
    single { AppDatabase.getDatabase(androidContext()) }

    // 2. Получаем DAO из базы данных
    single { get<AppDatabase>().productDao() }

    // 3. Передаем productDao в реализацию репозитория
    single<ProductRepository> { ProductRepositoryImpl(get()) }

    // 4. ViewModel
    viewModel { MainViewModel(get()) }
}