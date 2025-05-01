package com.ravikantsharma.core.domain.preference.usecase

import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.repository.UserPreferencesRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

data class SettingsPreferenceUseCase(
    val setPreferencesUseCase: SetPreferencesUseCase,
    val getPreferencesUseCase: GetPreferencesUseCase
)

class SetPreferencesUseCase(private val userPreferencesRepository: UserPreferencesRepository) {
    suspend operator fun invoke(userPreferences: UserPreferences): Result<Unit, DataError> {
        return userPreferencesRepository.insertPreference(userPreferences)
    }
}

class GetPreferencesUseCase(private val userPreferencesRepository: UserPreferencesRepository) {
    operator fun invoke(userId: Long): Flow<Result<UserPreferences, DataError>> {
        return userPreferencesRepository.getPreferences(userId)
    }
}