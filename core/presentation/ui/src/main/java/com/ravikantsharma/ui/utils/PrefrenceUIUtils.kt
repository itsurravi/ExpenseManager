package com.ravikantsharma.ui.utils

import com.ravikantsharma.core.domain.utils.getFormattedDayOfMonth
import com.ravikantsharma.core.domain.utils.getFormattedDayOfWeek
import com.ravikantsharma.core.domain.utils.getMonthAndDay
import com.ravikantsharma.core.presentation.designsystem.model.RecurringTypeUI
import java.time.LocalDateTime

fun RecurringTypeUI.getFormattedTitle(localDateTime: LocalDateTime): String {
    return when (this) {
        RecurringTypeUI.WEEKLY -> String.format(this.title, localDateTime.getFormattedDayOfWeek())
        RecurringTypeUI.MONTHLY -> String.format(
            this.title,
            "${localDateTime.getFormattedDayOfMonth()}th"
        )

        RecurringTypeUI.YEARLY -> String.format(
            this.title,
            "${localDateTime.getMonthAndDay()}th"
        )

        else -> this.title
    }
}