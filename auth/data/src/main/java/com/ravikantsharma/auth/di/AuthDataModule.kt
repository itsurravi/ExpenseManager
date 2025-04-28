package com.ravikantsharma.auth.di

import com.ravikantsharma.auth.domain.usecase.FormatExampleUseCase
import com.ravikantsharma.auth.domain.usecase.LoginUseCases
import com.ravikantsharma.auth.domain.usecase.IsUsernameDuplicateUseCase
import com.ravikantsharma.auth.domain.usecase.IsUsernameValidUseCase
import com.ravikantsharma.auth.domain.usecase.OnboardingPreferenceUseCases
import com.ravikantsharma.auth.domain.usecase.ValidateSelectedPreferences
import org.koin.dsl.module

val authDataModule = module {
    single { IsUsernameValidUseCase() }
    single { IsUsernameDuplicateUseCase() }
    single { LoginUseCases(get(), get()) }

    factory { ValidateSelectedPreferences() }
    factory { FormatExampleUseCase(get()) }
    single { OnboardingPreferenceUseCases(get(), get()) }
}