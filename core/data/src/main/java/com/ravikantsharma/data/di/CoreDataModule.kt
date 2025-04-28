package com.ravikantsharma.data.di

import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.security.EncryptionService
import com.ravikantsharma.data.security.AesEncryptionService
import com.ravikantsharma.data.security.KeyManager
import org.koin.dsl.module

val coreDataModule = module {
    single { NumberFormatter }
    single { KeyManager.getOrCreateSecretKey() }
    single<EncryptionService> { AesEncryptionService(get()) }
}