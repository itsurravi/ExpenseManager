package com.ravikantsharma.dashboard.data.di

import com.ravikantsharma.dashboard.domain.usecases.create_transactions.BuildTransactionUseCase
import com.ravikantsharma.dashboard.domain.usecases.create_transactions.CreateTransactionsUseCases
import com.ravikantsharma.dashboard.domain.usecases.create_transactions.GetTransactionHintUseCase
import com.ravikantsharma.dashboard.domain.usecases.create_transactions.IsExpenseCategoryVisibleUseCase
import com.ravikantsharma.dashboard.domain.usecases.create_transactions.IsValidInputUseCase
import com.ravikantsharma.dashboard.domain.usecases.dashboard.GetAllTransactionsDataUseCase
import com.ravikantsharma.dashboard.domain.usecases.dashboard.GetDashboardDataUseCase
import org.koin.dsl.module

val dashboardModule = module {
    single { GetDashboardDataUseCase(get(), get(), get()) }
    single { GetAllTransactionsDataUseCase(get(), get(), get()) }

    factory { GetTransactionHintUseCase() }
    factory { IsExpenseCategoryVisibleUseCase() }
    factory { IsValidInputUseCase() }
    factory { BuildTransactionUseCase(get(), get()) }
    single { CreateTransactionsUseCases(get(), get(), get(), get()) }
}