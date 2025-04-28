package com.ravikantsharma.data

import com.ravikantsharma.domain.formatting.NumberFormatter
import org.koin.dsl.module

val coreDataModule = module {
    single { NumberFormatter }
}