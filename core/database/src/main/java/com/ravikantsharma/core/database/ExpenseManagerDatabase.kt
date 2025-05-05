package com.ravikantsharma.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ravikantsharma.core.database.auth.dao.UserInfoDao
import com.ravikantsharma.core.database.auth.entity.UserInfoEntity
import com.ravikantsharma.core.database.preferences.dao.UserPreferenceDao
import com.ravikantsharma.core.database.preferences.entity.UserPreferenceEntity
import com.ravikantsharma.core.database.transactions.dao.TransactionsDao
import com.ravikantsharma.core.database.transactions.entity.TransactionEntity

@Database(
    entities = [
        UserInfoEntity::class,
        UserPreferenceEntity::class,
        TransactionEntity::class
    ],
    version = 1
)
abstract class ExpenseManagerDatabase : RoomDatabase() {
    abstract val userInfoDao: UserInfoDao
    abstract val userPreferenceDao: UserPreferenceDao
    abstract val transactionsDao: TransactionsDao
}