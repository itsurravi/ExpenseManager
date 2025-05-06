package com.ravikantsharma.settings.presentation.di

import com.ravikantsharma.settings.presentation.home.SettingsHomeViewModel
import com.ravikantsharma.settings.presentation.preference.SettingsPreferenceViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModelOf(::SettingsHomeViewModel)
    viewModelOf(::SettingsPreferenceViewModel)
}