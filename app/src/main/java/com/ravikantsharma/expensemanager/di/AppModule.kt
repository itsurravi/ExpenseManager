package com.ravikantsharma.expensemanager.di

import com.ravikantsharma.expensemanager.ExpenseManagerApp
import com.ravikantsharma.expensemanager.MainViewModel
import com.ravikantsharma.ui.NavigationRequestHandler
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as ExpenseManagerApp).applicationScope
    }

    viewModel { MainViewModel(savedStateHandle = get(), sessionUseCases = get()) }

    factory<NavigationRequestHandler> { get<MainViewModel>() }
}