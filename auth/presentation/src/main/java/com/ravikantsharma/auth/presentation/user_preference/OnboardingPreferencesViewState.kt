package com.ravikantsharma.auth.presentation.user_preference

import com.ravikantsharma.domain.formatting.NumberFormatter
import com.ravikantsharma.domain.model.DecimalSeparator
import com.ravikantsharma.domain.model.ExpenseFormat
import com.ravikantsharma.domain.model.ThousandsSeparator

data class OnboardingPreferencesViewState(
    val expenseFormat: ExpenseFormat = ExpenseFormat.MINUS_PREFIX,
    val currency: String = "USD",
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val thousandsSeparator: ThousandsSeparator = ThousandsSeparator.COMMA,
    val enableStartTracking: Boolean = false,
    val exampleFormat: String = NumberFormatter.formatAmount(
        amount = 12234.12,
        expenseFormat = expenseFormat,
        decimalSeparator = decimalSeparator,
        thousandsSeparator = thousandsSeparator,
        currencySymbol = "$"
    )
)