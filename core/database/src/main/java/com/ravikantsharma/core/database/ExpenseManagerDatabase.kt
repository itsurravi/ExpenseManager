package com.ravikantsharma.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ravikantsharma.core.database.auth.dao.UserInfoDao
import com.ravikantsharma.core.database.auth.entity.UserInfoEntity
import com.ravikantsharma.core.database.preferences.dao.UserPreferenceDao
import com.ravikantsharma.core.database.preferences.entity.UserPreferenceEntity

@Database(
    entities = [
        UserInfoEntity::class,
        UserPreferenceEntity::class
    ],
    version = 1
)
abstract class ExpenseManagerDatabase : RoomDatabase() {
    abstract val userInfoDao: UserInfoDao
    abstract val userPreferenceDao: UserPreferenceDao
}