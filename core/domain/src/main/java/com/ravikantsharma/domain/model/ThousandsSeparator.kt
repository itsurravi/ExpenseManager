package com.ravikantsharma.domain.model

import java.util.Locale

enum class ThousandsSeparator : PreferenceOption {
    DOT,
    COMMA,
    SPACE;

    override fun displayText(number: Double): String {
        val locale = Locale.US
        val formatterNumber = String.format(locale, "%,.2f", number)

        return when (this) {
            DOT -> formatterNumber.replace(',', '.')
            COMMA -> formatterNumber
            SPACE -> formatterNumber.replace(',', ' ')
        }
    }

    companion object {
        fun fromSaveValue(value: String): ThousandsSeparator? {
            return when (value) {
                "." -> DOT
                "," -> COMMA
                " " -> SPACE
                else -> null
            }
        }
    }
}