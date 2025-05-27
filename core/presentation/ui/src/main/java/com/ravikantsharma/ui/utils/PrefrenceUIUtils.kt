package com.ravikantsharma.ui.utils

import com.ravikantsharma.core.domain.utils.CalendarUtils
import com.ravikantsharma.core.presentation.designsystem.model.RecurringTypeUI

fun RecurringTypeUI.getFormattedTitle(): String {
    return when (this) {
        RecurringTypeUI.WEEKLY -> String.format(this.title, CalendarUtils.getCurrentDayOfWeek())
        RecurringTypeUI.MONTHLY -> String.format(
            this.title,
            "${CalendarUtils.getCurrentDayOfMonth()}th"
        )

        RecurringTypeUI.YEARLY -> String.format(
            this.title,
            "${CalendarUtils.getCurrentMonthAndDay()}th"
        )

        else -> this.title
    }
}