package com.ravikantsharma.data.di

import com.ravikantsharma.core.domain.formatting.NumberFormatter
import org.koin.dsl.module

val coreDataModule = module {
    single { NumberFormatter }
}