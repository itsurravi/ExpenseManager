package com.ravikantsharma.core.domain.model

import java.math.BigDecimal

interface PreferenceOption {
    fun displayText(
        number: BigDecimal,
        currency: Currency? = null,
        keepDecimal: Boolean = true
    ): String
}

interface SettingsPreferenceOption {
    fun displayText(): String
    fun getValueInLong(): Long
}