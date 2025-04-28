package com.ravikantsharma.auth.domain.usecase

import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.model.Currency
import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ExpenseFormat
import com.ravikantsharma.core.domain.model.ThousandsSeparator
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.preference.repository.UserPreferencesRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result

data class OnboardingPreferenceUseCases(
    val validateSelectedPreferences: ValidateSelectedPreferences,
    val formatExampleUseCase: FormatExampleUseCase,
    val setPreferencesUseCase: SetPreferencesUseCase
)

class ValidateSelectedPreferences {
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

class FormatExampleUseCase(private val numberFormatter: NumberFormatter) {
    operator fun invoke(
        expenseFormat: ExpenseFormat,
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator,
        currency: Currency
    ): String {
        return numberFormatter.formatAmount(
            amount = -10382.45,
            expenseFormat = expenseFormat,
            decimalSeparator = decimalSeparator,
            thousandsSeparator = thousandsSeparator,
            currency = currency
        )
    }
}

class SetPreferencesUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(userPreferences: UserPreferences): Result<Unit, DataError> {
        return userPreferencesRepository.insertPreference(userPreferences)
    }
}