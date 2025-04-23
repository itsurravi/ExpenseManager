package com.ravikantsharma.auth.di

import com.ravikantsharma.auth.domain.usecase.AuthUseCases
import com.ravikantsharma.auth.domain.usecase.IsUsernameDuplicateUseCase
import com.ravikantsharma.auth.domain.usecase.IsUsernameValidUseCase
import org.koin.dsl.module

val authDataModule = module {
    single { IsUsernameValidUseCase() }
    single { IsUsernameDuplicateUseCase() }
    single { AuthUseCases(get(), get()) }
}