package com.ravikantsharma.auth.data.di

import com.ravikantsharma.auth.data.repository.UserInfoRepositoryImpl
import com.ravikantsharma.auth.domain.repository.UserInfoRepository
import com.ravikantsharma.auth.domain.usecase.DecryptPinUseCase
import com.ravikantsharma.auth.domain.usecase.EncryptPinUseCase
import com.ravikantsharma.auth.domain.usecase.EncryptionUseCases
import com.ravikantsharma.auth.domain.usecase.FormatExampleUseCase
import com.ravikantsharma.auth.domain.usecase.IsUsernameDuplicateUseCase
import com.ravikantsharma.auth.domain.usecase.IsUsernameValidUseCase
import com.ravikantsharma.auth.domain.usecase.LoginUseCases
import com.ravikantsharma.auth.domain.usecase.OnboardingPreferenceUseCases
import com.ravikantsharma.auth.domain.usecase.RegisterUseCases
import com.ravikantsharma.auth.domain.usecase.RegisterUserUseCase
import com.ravikantsharma.auth.domain.usecase.SetPreferencesUseCase
import com.ravikantsharma.auth.domain.usecase.ValidateSelectedPreferences
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single { IsUsernameValidUseCase() }
    single { IsUsernameDuplicateUseCase() }
    single { LoginUseCases(get(), get()) }

    factory { ValidateSelectedPreferences() }
    factory { FormatExampleUseCase(get()) }
    factory { SetPreferencesUseCase(get()) }
    single { OnboardingPreferenceUseCases(get(), get(), get()) }

    factory { EncryptPinUseCase(get()) }
    factory { DecryptPinUseCase(get()) }
    single { EncryptionUseCases(get(), get()) }

    singleOf(::UserInfoRepositoryImpl).bind<UserInfoRepository>()
    factory { RegisterUserUseCase(get()) }
    single { RegisterUseCases(get()) }
}