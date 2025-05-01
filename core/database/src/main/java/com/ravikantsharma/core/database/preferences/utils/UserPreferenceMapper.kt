package com.ravikantsharma.core.database.preferences.utils

import com.ravikantsharma.core.database.preferences.entity.UserPreferenceEntity
import com.ravikantsharma.core.domain.model.Currency
import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ExpenseFormat
import com.ravikantsharma.core.domain.model.LockoutDuration
import com.ravikantsharma.core.domain.model.PinAttempts
import com.ravikantsharma.core.domain.model.SessionDuration
import com.ravikantsharma.core.domain.model.ThousandsSeparator
import com.ravikantsharma.core.domain.preference.model.UserPreferences

fun UserPreferenceEntity.toUserPreferences(): UserPreferences {
    return UserPreferences(
        userId = userId,
        expenseFormat = ExpenseFormat.valueOf(expenseFormat.name),
        currency = Currency.valueOf(currency.name),
        decimalSeparator = DecimalSeparator.valueOf(decimalSeparator.name),
        thousandsSeparator = ThousandsSeparator.valueOf(thousandsSeparator.name),
        isBiometricEnabled = isBiometricEnabled,
        sessionDuration = SessionDuration.valueOf(sessionDuration.name),
        lockOutDuration = LockoutDuration.valueOf(lockOutDuration.name),
        allowedPinAttempts = PinAttempts.valueOf(allowedPinAttempts.name)
    )
}

fun UserPreferences.toUserPreferenceEntity(): UserPreferenceEntity {
    return UserPreferenceEntity(
        userId = userId,
        expenseFormat = expenseFormat,
        currency = currency,
        decimalSeparator = decimalSeparator,
        thousandsSeparator = thousandsSeparator,
        isBiometricEnabled = isBiometricEnabled,
        sessionDuration = sessionDuration,
        lockOutDuration = lockOutDuration,
        allowedPinAttempts = allowedPinAttempts
    )
}