package com.ravikantsharma.settings.presentation.preference

import com.ravikantsharma.core.domain.model.Currency
import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ExpenseFormat
import com.ravikantsharma.core.domain.model.ThousandsSeparator

sealed interface SettingsPreferencesAction {
    data class OnExpenseFormatUpdate(val format: ExpenseFormat) : SettingsPreferencesAction
    data class OnCurrencyUpdate(val currency: Currency) : SettingsPreferencesAction
    data class OnDecimalSeparatorUpdate(val format: DecimalSeparator) : SettingsPreferencesAction
    data class OnThousandsSeparatorUpdate(val format: ThousandsSeparator) :
        SettingsPreferencesAction

    data object OnBackClicked : SettingsPreferencesAction
    data object OnSaveClicked : SettingsPreferencesAction
}