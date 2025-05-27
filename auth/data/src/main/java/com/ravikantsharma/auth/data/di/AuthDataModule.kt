package com.ravikantsharma.auth.data.di

import com.ravikantsharma.auth.domain.usecase.InitiateLoginUseCase
import com.ravikantsharma.auth.domain.usecase.IsUserNameDuplicateUseCase
import com.ravikantsharma.auth.domain.usecase.IsUsernameValidUseCase
import com.ravikantsharma.auth.domain.usecase.LoginUseCases
import com.ravikantsharma.auth.domain.usecase.OnboardingPreferenceUseCases
import com.ravikantsharma.auth.domain.usecase.RegisterUseCases
import com.ravikantsharma.auth.domain.usecase.RegisterUserUseCase
import com.ravikantsharma.auth.domain.usecase.ValidateSelectedPreferences
import com.ravikantsharma.core.domain.preference.usecase.SetPreferencesUseCase
import org.koin.dsl.module

val authDataModule = module {
    single { IsUsernameValidUseCase() }
    single { InitiateLoginUseCase(get()) }
    single { LoginUseCases(get(), get()) }

    factory { ValidateSelectedPreferences() }
    factory { SetPreferencesUseCase(get()) }
    single { OnboardingPreferenceUseCases(get()) }

    factory { RegisterUserUseCase(get()) }
    factory { IsUserNameDuplicateUseCase(get()) }
    single { RegisterUseCases(get(), get()) }
}