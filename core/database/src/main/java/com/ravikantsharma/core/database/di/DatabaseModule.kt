package com.ravikantsharma.core.database.di

import androidx.room.Room
import com.ravikantsharma.core.database.ExpenseManagerDatabase
import com.ravikantsharma.core.database.auth.data_source.RoomLocalUserInfoDataSource
import com.ravikantsharma.core.database.preferences.data_source.RoomLocalUserPreferenceDataSource
import com.ravikantsharma.core.domain.auth.data_source.LocalUserInfoDataSource
import com.ravikantsharma.core.domain.preference.data_source.LocalPreferencesDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ExpenseManagerDatabase::class.java,
            "expense_manager_database.db"
        ).build()
    }

    single { get<ExpenseManagerDatabase>().userInfoDao }
    single { get<ExpenseManagerDatabase>().userPreferenceDao }

    singleOf(::RoomLocalUserInfoDataSource).bind<LocalUserInfoDataSource>()
    singleOf(::RoomLocalUserPreferenceDataSource).bind<LocalPreferencesDataSource>()
}