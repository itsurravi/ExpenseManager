package com.ravikantsharma.data.repository

import com.ravikantsharma.core.domain.preference.data_source.LocalPreferencesDataSource
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.repository.UserPreferencesRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl(
    private val localPreferencesDataSource: LocalPreferencesDataSource
): UserPreferencesRepository {
    override suspend fun insertPreference(preferences: UserPreferences): Result<Unit, DataError> {
        return localPreferencesDataSource.insertPreference(preferences)
    }

    override fun getPreferences(userId: Long): Flow<Result<UserPreferences, DataError>> {
        return localPreferencesDataSource.getPreferences(userId)
    }
}