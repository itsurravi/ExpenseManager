package com.ravikantsharma.auth.domain.usecase

import com.ravikantsharma.core.domain.auth.model.UserInfo
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.usecase.SetPreferencesUseCase
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.session_management.domain.model.SessionData
import com.ravikantsharma.session_management.domain.usecases.SaveSessionUseCase

data class OnboardingPreferenceUseCases(
    val registerUserAndSavePreferencesUseCase: RegisterUserAndSavePreferencesUseCase
)

class RegisterUserAndSavePreferencesUseCase(
    private val registerUserUseCase: RegisterUserUseCase,
    private val setPreferencesUseCase: SetPreferencesUseCase,
    private val saveSessionUseCase: SaveSessionUseCase
) {
    suspend operator fun invoke(
        userInfo: UserInfo,
        preferences: UserPreferences
    ): Result<Unit, DataError> {

        val userId = when (val userIdResult = registerUserUseCase(userInfo)) {
            is Result.Error -> return Result.Error(userIdResult.error)
            is Result.Success -> userIdResult.data
        }

        val updatedPreferences = preferences.copy(userId = userId)
        val preferencesResult = setPreferencesUseCase(updatedPreferences)
        if (preferencesResult is Result.Error) {
            return Result.Error(preferencesResult.error)
        }

        val sessionData = SessionData(
            userId = userId,
            userName = userInfo.username,
            sessionExpiryTime = 0
        )
        saveSessionUseCase(sessionData)

        return Result.Success(Unit)
    }
}