package com.ravikantsharma.core.domain.preference.usecase

import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ThousandsSeparator
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.repository.UserPreferencesRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

data class PreferenceUseCase(
    val setPreferencesUseCase: SetPreferencesUseCase,
    val getPreferencesUseCase: GetPreferencesUseCase,
    val isValidPreferenceUseCase: ValidateSelectedPreferenceUseCase,
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

class ValidateSelectedPreferenceUseCase {
    operator fun invoke(
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator
    ): Boolean {
        return when {
            (decimalSeparator == DecimalSeparator.DOT && thousandsSeparator == ThousandsSeparator.DOT) ||
                    (decimalSeparator == DecimalSeparator.COMMA && thousandsSeparator == ThousandsSeparator.COMMA) -> false

            else -> true
        }
    }
}