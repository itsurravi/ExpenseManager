package com.ravikantsharma.domain.formatting

import com.ravikantsharma.domain.model.DecimalSeparator
import com.ravikantsharma.domain.model.ExpenseFormat
import com.ravikantsharma.domain.model.ThousandsSeparator
import java.util.Locale
import kotlin.math.abs

object NumberFormatter {
    fun formatAmount(
        amount: Double,
        expenseFormat: ExpenseFormat,
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator,
        currencySymbol: String
    ): String {
        val isNegative = amount < 0
        val absoluteAmount = abs(amount)

        var formattedNumber = String.format(Locale.US, "%,.2f", absoluteAmount)

        formattedNumber = when (decimalSeparator) {
            DecimalSeparator.DOT -> formattedNumber
            DecimalSeparator.COMMA -> formattedNumber.replace(".", ",")
        }

        formattedNumber = when (thousandsSeparator) {
            ThousandsSeparator.DOT -> formattedNumber.replace(".", ",")
            ThousandsSeparator.COMMA -> formattedNumber
            ThousandsSeparator.SPACE -> formattedNumber.replace(",", "")
        }

        return when {
            isNegative && expenseFormat == ExpenseFormat.MINUS_PREFIX -> "-$currencySymbol$formattedNumber"
            isNegative && expenseFormat == ExpenseFormat.BRACKETS -> "($currencySymbol$formattedNumber)"
            else -> "$currencySymbol$formattedNumber"
        }
    }
}