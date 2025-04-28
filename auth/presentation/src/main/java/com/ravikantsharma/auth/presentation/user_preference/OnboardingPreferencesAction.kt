package com.ravikantsharma.auth.presentation.user_preference

import com.ravikantsharma.core.domain.model.Currency
import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ExpenseFormat
import com.ravikantsharma.core.domain.model.ThousandsSeparator

sealed interface OnboardingPreferencesAction {
    data class OnExpenseFormatUpdate(val format: ExpenseFormat) : OnboardingPreferencesAction
    data class OnCurrencyUpdate(val currency: Currency) : OnboardingPreferencesAction
    data class OnDecimalSeparatorUpdate(val format: DecimalSeparator) : OnboardingPreferencesAction
    data class OnThousandsSeparatorUpdate(val format: ThousandsSeparator) :
        OnboardingPreferencesAction
    data object OnBackClicked : OnboardingPreferencesAction
    data object OnStartClicked : OnboardingPreferencesAction
}