package com.ravikantsharma.expensemanager

import android.app.Application
import com.ravikantsharma.auth.data.di.authDataModule
import com.ravikantsharma.auth.presentation.di.authViewModelModule
import com.ravikantsharma.core.database.di.databaseModule
import com.ravikantsharma.data.di.coreDataModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class ExpenseManagerApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@ExpenseManagerApp)
            modules(
                authViewModelModule,
                authDataModule,
                coreDataModule,
                databaseModule
            )

        }
    }
}