package com.ravikantsharma.dashboard.presentation.mapper

import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ExpenseFormat
import com.ravikantsharma.core.domain.model.ThousandsSeparator
import com.ravikantsharma.core.presentation.designsystem.components.text_field.DecimalSeparatorUI
import com.ravikantsharma.core.presentation.designsystem.components.text_field.ExpenseFormatUI
import com.ravikantsharma.core.presentation.designsystem.components.text_field.ThousandsSeparatorUI

fun ExpenseFormat.toExpenseFormatUI(): ExpenseFormatUI {
    return when (this) {
        ExpenseFormat.MINUS_PREFIX -> ExpenseFormatUI.MINUS_SIGN
        ExpenseFormat.BRACKETS -> ExpenseFormatUI.PARENTHESES
    }
}

fun DecimalSeparator.toDecimalSeparatorUI(): DecimalSeparatorUI {
    return when (this) {
        DecimalSeparator.DOT -> DecimalSeparatorUI.DOT
        DecimalSeparator.COMMA -> DecimalSeparatorUI.COMMA
    }
}

fun ThousandsSeparator.toThousandsSeparatorUI(): ThousandsSeparatorUI {
    return when (this) {
        ThousandsSeparator.DOT -> ThousandsSeparatorUI.DOT
        ThousandsSeparator.COMMA -> ThousandsSeparatorUI.COMMA
        ThousandsSeparator.SPACE -> ThousandsSeparatorUI.SPACE
    }
}