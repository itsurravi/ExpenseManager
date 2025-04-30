package com.ravikantsharma.dashboard.presentation.di

import com.ravikantsharma.dashboard.presentation.dashboard.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardPresentationModule = module {
    viewModelOf(::DashboardViewModel)
}