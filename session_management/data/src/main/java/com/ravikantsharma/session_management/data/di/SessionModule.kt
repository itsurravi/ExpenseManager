package com.ravikantsharma.session_management.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ravikantsharma.session_management.data.repository.SessionRepositoryImpl
import com.ravikantsharma.session_management.domain.repository.SessionRepository
import com.ravikantsharma.session_management.domain.usecases.CheckSessionExpiryUseCase
import com.ravikantsharma.session_management.domain.usecases.ClearSessionUseCase
import com.ravikantsharma.session_management.domain.usecases.GetSessionStatusUseCase
import com.ravikantsharma.session_management.domain.usecases.SessionUseCases
import com.ravikantsharma.session_management.domain.usecases.StartSessionUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

val sessionModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }

    factory { StartSessionUseCase(get()) }
    factory { GetSessionStatusUseCase(get()) }
    factory { ClearSessionUseCase(get()) }
    factory { CheckSessionExpiryUseCase(get()) }

    single { SessionUseCases(get(), get(), get(), get()) }

    singleOf(::SessionRepositoryImpl).bind<SessionRepository>()
}