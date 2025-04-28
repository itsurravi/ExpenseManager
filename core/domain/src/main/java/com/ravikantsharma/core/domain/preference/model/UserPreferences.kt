package com.ravikantsharma.core.domain.preference.model

import com.ravikantsharma.core.domain.model.Currency
import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ExpenseFormat
import com.ravikantsharma.core.domain.model.ThousandsSeparator

data class UserPreferences(
    val userId: Long,
    val expenseFormat: ExpenseFormat,
    val currency: Currency,
    val decimalSeparator: DecimalSeparator,
    val thousandsSeparator: ThousandsSeparator
)
