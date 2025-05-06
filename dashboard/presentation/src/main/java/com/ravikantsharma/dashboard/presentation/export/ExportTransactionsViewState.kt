package com.ravikantsharma.dashboard.presentation.export

import com.ravikantsharma.core.domain.export.model.ExportType

data class ExportTransactionsViewState(
    val exportType: ExportType = ExportType.LAST_THREE_MONTH
)