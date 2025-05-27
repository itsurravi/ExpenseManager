package com.ravikantsharma.auth.data.di

import com.ravikantsharma.auth.domain.usecase.AppendDigitUseCase
import com.ravikantsharma.auth.domain.usecase.CreatePinUseCases
import com.ravikantsharma.auth.domain.usecase.DeleteDigitUseCase
import com.ravikantsharma.auth.domain.usecase.InitiateLoginUseCase
import com.ravikantsharma.auth.domain.usecase.IsUserNameDuplicateUseCase
import com.ravikantsharma.auth.domain.usecase.IsUsernameValidUseCase
import com.ravikantsharma.auth.domain.usecase.LoginUseCases
import com.ravikantsharma.auth.domain.usecase.OnboardingPreferenceUseCases
import com.ravikantsharma.auth.domain.usecase.RegisterUseCases
import com.ravikantsharma.auth.domain.usecase.RegisterUserAndSavePreferencesUseCase
import com.ravikantsharma.auth.domain.usecase.RegisterUserUseCase
import com.ravikantsharma.auth.domain.usecase.ValidatePinMatchUseCase
import org.koin.dsl.module

val authDataModule = module {
    // Login
    single { IsUsernameValidUseCase() }
    factory { InitiateLoginUseCase(get()) }
    single { LoginUseCases(get(), get()) }

    // Onboarding Preferences
    factory { RegisterUserAndSavePreferencesUseCase(get(), get(), get()) }
    single { OnboardingPreferenceUseCases(get()) }

    // Register User
    factory { RegisterUserUseCase(get()) }
    factory { IsUserNameDuplicateUseCase(get()) }
    single { RegisterUseCases(get(), get()) }

    // Create Pin
    factory { AppendDigitUseCase() }
    factory { DeleteDigitUseCase() }
    factory { ValidatePinMatchUseCase() }
    single { CreatePinUseCases(get(), get(), get()) }
}