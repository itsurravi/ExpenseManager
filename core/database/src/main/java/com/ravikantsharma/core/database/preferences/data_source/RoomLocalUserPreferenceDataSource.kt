package com.ravikantsharma.core.database.preferences.data_source

import com.ravikantsharma.core.database.preferences.dao.UserPreferenceDao
import com.ravikantsharma.core.database.preferences.utils.toUserPreferenceEntity
import com.ravikantsharma.core.database.preferences.utils.toUserPreferences
import com.ravikantsharma.core.domain.preference.data_source.LocalPreferencesDataSource
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException


class RoomLocalUserPreferenceDataSource(
    private val userPreferenceDao: UserPreferenceDao
) : LocalPreferencesDataSource {

    override suspend fun insertPreference(preferences: UserPreferences): Result<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            try {
                userPreferenceDao.upsertUserPreference(preferences.toUserPreferenceEntity())
                Result.Success(Unit)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
            }
        }
    }

    override fun getPreferences(userId: Long): Flow<Result<UserPreferences, DataError>> {
        return userPreferenceDao.getUserPreference(userId)
            .map { preferenceEntity ->
                preferenceEntity?.let {
                    Result.Success(it.toUserPreferences())
                } ?: Result.Error(DataError.Local.PREFERENCE_FETCH_ERROR)
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }
}