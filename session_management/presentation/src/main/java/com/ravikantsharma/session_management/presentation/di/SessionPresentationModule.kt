package com.ravikantsharma.session_management.presentation.di

import com.ravikantsharma.session_management.presentation.pin_prompt.PinPromptViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sessionPresentationModule = module {
    viewModelOf(::PinPromptViewModel)
}