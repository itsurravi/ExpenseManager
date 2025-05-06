package com.ravikantsharma.core.data.di

import com.ravikantsharma.core.data.repository.TransactionRepositoryImpl
import com.ravikantsharma.core.data.repository.UserInfoRepositoryImpl
import com.ravikantsharma.core.data.repository.UserPreferencesRepositoryImpl
import com.ravikantsharma.core.data.security.AesEncryptionService
import com.ravikantsharma.core.data.security.KeyManager
import com.ravikantsharma.core.domain.auth.repository.UserInfoRepository
import com.ravikantsharma.core.domain.auth.usecases.GetUserInfoUseCase
import com.ravikantsharma.core.domain.auth.usecases.UserInfoUseCases
import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.preference.repository.UserPreferencesRepository
import com.ravikantsharma.core.domain.preference.usecase.GetPreferencesUseCase
import com.ravikantsharma.core.domain.preference.usecase.SetPreferencesUseCase
import com.ravikantsharma.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.ravikantsharma.core.domain.preference.usecase.ValidateSelectedPreferences
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.core.domain.transactions.repository.TransactionRepository
import com.ravikantsharma.core.domain.transactions.usecases.GetDueRecurringTransactionsUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetRecurringTransactionSeriesUseCase
import com.ravikantsharma.core.domain.transactions.usecases.GetTransactionsForUserUseCase
import com.ravikantsharma.core.domain.transactions.usecases.InsertTransactionUseCase
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

    factory { GetUserInfoUseCase(get(), get()) }
    single { UserInfoUseCases(get()) }

    factory { InsertTransactionUseCase(get()) }
    factory { GetTransactionsForUserUseCase(get()) }
    factory { GetRecurringTransactionSeriesUseCase(get()) }
    factory { GetDueRecurringTransactionsUseCase(get()) }
    single { TransactionUseCases(get(), get(), get(), get()) }
    singleOf(::TransactionRepositoryImpl).bind<TransactionRepository>()
}