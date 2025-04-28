package com.ravikantsharma.auth.di

import com.ravikantsharma.auth.domain.usecase.LoginUseCases
import com.ravikantsharma.auth.domain.usecase.IsUsernameDuplicateUseCase
import com.ravikantsharma.auth.domain.usecase.IsUsernameValidUseCase
import com.ravikantsharma.auth.domain.usecase.OnboardingPreferenceUseCases
import org.koin.dsl.module

val authDataModule = module {
    single { IsUsernameValidUseCase() }
    single { IsUsernameDuplicateUseCase() }
    single { LoginUseCases(get(), get()) }
    single { OnboardingPreferenceUseCases(get(), get()) }
}