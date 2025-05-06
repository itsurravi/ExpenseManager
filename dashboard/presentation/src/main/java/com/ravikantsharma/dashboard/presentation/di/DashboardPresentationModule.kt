package com.ravikantsharma.dashboard.presentation.di

import com.ravikantsharma.dashboard.presentation.all_transactions.AllTransactionsViewModel
import com.ravikantsharma.dashboard.presentation.create_screen.CreateTransactionViewModel
import com.ravikantsharma.dashboard.presentation.dashboard.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardPresentationModule = module {
    viewModelOf(::DashboardViewModel)
    viewModelOf(::CreateTransactionViewModel)
    viewModelOf(::AllTransactionsViewModel)
}