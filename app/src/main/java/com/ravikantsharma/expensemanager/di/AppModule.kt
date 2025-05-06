package com.ravikantsharma.expensemanager.di

import com.ravikantsharma.expensemanager.ExpenseManagerApp
import com.ravikantsharma.expensemanager.MainViewModel
import com.ravikantsharma.ui.NavigationRequestHandler
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as ExpenseManagerApp).applicationScope
    }
    viewModelOf(::MainViewModel)

    factory<NavigationRequestHandler> {
        get<MainViewModel>()
    }
}