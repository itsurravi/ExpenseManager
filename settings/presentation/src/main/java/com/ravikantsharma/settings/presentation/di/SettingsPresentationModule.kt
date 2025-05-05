package com.ravikantsharma.settings.presentation.di

import com.ravikantsharma.settings.presentation.home.SettingsHomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModelOf(::SettingsHomeViewModel)
}