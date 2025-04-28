package com.ravikantsharma.core.database.preferences.data_source

import com.ravikantsharma.core.database.preferences.dao.UserPreferenceDao
import com.ravikantsharma.core.database.preferences.utils.toUserPreferenceEntity
import com.ravikantsharma.core.database.preferences.utils.toUserPreferences
import com.ravikantsharma.core.domain.preference.data_source.LocalPreferencesDataSource
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


class RoomLocalUserPreferenceDataSource(
    private val userPreferenceDao: UserPreferenceDao
) : LocalPreferencesDataSource {

    override suspend fun insertPreference(preferences: UserPreferences): Result<Unit, DataError> {
        return try {
            val result = userPreferenceDao.upsertUserPreference(preferences.toUserPreferenceEntity())

            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERT_PREFERENCE_ERROR)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override fun getPreferences(userId: Long): Flow<Result<UserPreferences, DataError>> {
        return userPreferenceDao.getUserPreference(userId)
            .map { preferenceEntity ->
                preferenceEntity?.let {
                    Result.Success(it.toUserPreferences())
                } ?: Result.Error(DataError.Local.PREFERENCE_FETCH_ERROR)
            }
            .catch {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }
}