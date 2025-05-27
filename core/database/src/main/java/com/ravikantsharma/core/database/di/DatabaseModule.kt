package com.ravikantsharma.core.database.di

import androidx.room.Room
import com.ravikantsharma.core.database.BuildConfig
import com.ravikantsharma.core.database.ExpenseManagerDatabase
import com.ravikantsharma.core.database.auth.data_source.RoomLocalUserInfoDataSource
import com.ravikantsharma.core.database.preferences.data_source.RoomLocalUserPreferenceDataSource
import com.ravikantsharma.core.database.security.DatabaseSecurity
import com.ravikantsharma.core.database.transactions.data_source.RoomTransactionDataSource
import com.ravikantsharma.core.domain.auth.data_source.LocalUserInfoDataSource
import com.ravikantsharma.core.domain.preference.data_source.LocalPreferencesDataSource
import com.ravikantsharma.core.domain.transactions.data_source.LocalTransactionDataSource
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        val isEncrypted = BuildConfig.IS_DATABASE_ENCRYPTION_ENABLED
        val dbName = "expense_manager_database.db"

        val builder = Room.databaseBuilder(
            androidApplication(),
            ExpenseManagerDatabase::class.java,
            dbName
        )

        if (isEncrypted) {
            val passphrase = DatabaseSecurity.getDatabasePassphrase(androidApplication())
            val factory = SupportFactory(SQLiteDatabase.getBytes(passphrase.toCharArray()))
            builder.openHelperFactory(factory)
        }

        builder.build()
    }

    single { get<ExpenseManagerDatabase>().userInfoDao }
    single { get<ExpenseManagerDatabase>().userPreferenceDao }
    single { get<ExpenseManagerDatabase>().transactionsDao }

    singleOf(::RoomLocalUserInfoDataSource).bind<LocalUserInfoDataSource>()
    singleOf(::RoomLocalUserPreferenceDataSource).bind<LocalPreferencesDataSource>()
    singleOf(::RoomTransactionDataSource).bind<LocalTransactionDataSource>()
}