package com.ravikantsharma.auth.presentation.di

import com.ravikantsharma.auth.presentation.create_pin.CreatePinViewModel
import com.ravikantsharma.auth.presentation.login.LoginViewModel
import com.ravikantsharma.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::CreatePinViewModel)
}