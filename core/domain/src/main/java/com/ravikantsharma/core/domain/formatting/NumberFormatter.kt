package com.ravikantsharma.core.domain.formatting

import com.ravikantsharma.core.domain.model.Currency
import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ExpenseFormat
import com.ravikantsharma.core.domain.model.ThousandsSeparator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

object NumberFormatter {
    fun formatAmount(
        amount: Double,
        expenseFormat: ExpenseFormat,
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator,
        currency: Currency
    ): String {
        val isNegativeNumber = amount < 0
        val absoluteAmount = abs(amount)

        val numberFormat = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
        val symbols = DecimalFormatSymbols(Locale.US)

        symbols.decimalSeparator = decimalSeparator.toValue()
        symbols.groupingSeparator = thousandsSeparator.toValue()

        numberFormat.decimalFormatSymbols = symbols
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.isGroupingUsed = true

        val formattedNumber = numberFormat.format(absoluteAmount)

        val finalFormattedAmount = if (isNegativeNumber) {
            expenseFormat.toValue(formattedNumber)
        } else formattedNumber

        return "${currency.symbol}$finalFormattedAmount"
    }
}