package com.ravikantsharma.auth.presentation.user_preference

sealed interface OnboardingPreferencesAction {
    data class OnExpenseFormatUpdate(val format: String) : OnboardingPreferencesAction
    data class OnCurrencyUpdate(val format: String) : OnboardingPreferencesAction
    data class OnDecimalSeparatorUpdate(val format: String) : OnboardingPreferencesAction
    data class OnThousandSeparatorUpdate(val format: String) : OnboardingPreferencesAction
    data object OnBackClicked : OnboardingPreferencesAction
    data object OnStartClicked : OnboardingPreferencesAction
}