package com.ravikantsharma.core.data.di

import com.ravikantsharma.core.data.export.repository.ExportRepositoryImpl
import com.ravikantsharma.core.data.repository.TransactionRepositoryImpl
import com.ravikantsharma.core.data.repository.UserInfoRepositoryImpl
import com.ravikantsharma.core.data.repository.UserPreferencesRepositoryImpl
import com.ravikantsharma.core.data.security.AesEncryptionService
import com.ravikantsharma.core.data.security.KeyManager
import com.ravikantsharma.core.domain.auth.repository.UserInfoRepository
import com.ravikantsharma.core.domain.auth.usecases.GetUserInfoUseCase
import com.ravikantsharma.core.domain.auth.usecases.UserInfoUseCases
import com.ravikantsharma.core.domain.export.repository.ExportRepository
import com.ravikantsharma.core.domain.export.usecases.ExportTransactionUseCase
import com.ravikantsharma.core.domain.export.usecases.ExportTransactionsUseCases
import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.preference.repository.UserPreferencesRepository
import com.ravikantsharma.core.domain.preference.usecase.GetPreferencesUseCase
import com.ravikantsharma.core.domain.preference.usecase.SetPreferencesUseCase
import com.ravikantsharma.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.ravikantsharma.core.domain.preference.usecase.ValidateSelectedPreferences
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.core.domain.transactions.repository.TransactionRepository
import com.ravikantsharma.core.domain.transactions.usecases.GetAccountBalanceUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetDueRecurringTransactionsUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetLargestTransactionUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetMostPopularExpenseCategoryUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetNextRecurringDateUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetPreviousWeekTotalUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetTransactionsForUserUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetTransactionsGroupedByDateUseCase
import com.ravikantsharma.core.domain.transactions.usecases.InsertTransactionUseCase
import com.ravikantsharma.core.domain.transactions.usecases.ProcessRecurringTransactionsUseCase
import com.ravikantsharma.core.domain.transactions.usecases.TransactionUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single { NumberFormatter }
    single { KeyManager.getOrCreateSecretKey() }
    single<EncryptionService> { AesEncryptionService(get()) }

    singleOf(::UserPreferencesRepositoryImpl).bind<UserPreferencesRepository>()

    factory { SetPreferencesUseCase(get()) }
    factory { GetPreferencesUseCase(get()) }
    factory { ValidateSelectedPreferences() }
    single { SettingsPreferenceUseCase(get(), get(), get()) }

    singleOf(::UserInfoRepositoryImpl).bind<UserInfoRepository>()

    factory { GetUserInfoUseCase(get()) }
    single { UserInfoUseCases(get()) }

    factory { InsertTransactionUseCase(get()) }
    factory { GetTransactionsForUserUseCase(get()) }
    factory { GetDueRecurringTransactionsUseCase(get()) }
    factory { GetAccountBalanceUseCase(get()) }
    factory { GetMostPopularExpenseCategoryUseCase(get()) }
    factory { GetLargestTransactionUseCase(get()) }
    factory { GetPreviousWeekTotalUseCase(get()) }
    factory { GetTransactionsGroupedByDateUseCase() }
    factory { GetNextRecurringDateUseCase() }
    factory { ProcessRecurringTransactionsUseCase(get(), get(), get()) }
    single {
        TransactionUseCases(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    singleOf(::TransactionRepositoryImpl).bind<TransactionRepository>()

    singleOf(::ExportRepositoryImpl).bind<ExportRepository>()
    factory { ExportTransactionUseCase(get(), get()) }
    single { ExportTransactionsUseCases(get()) }
}