package com.ravikantsharma.core.domain.preference.repository

import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun insertPreference(preferences: UserPreferences): Result<Unit, DataError>

    fun getPreferences(userId: Long): Flow<Result<UserPreferences, DataError>>
}