package com.ravikantsharma.core.domain.model

interface PreferenceOption {
    fun displayText(
        number: Double,
        currency: Currency? = null,
        keepDecimal: Boolean = true
    ): String
}