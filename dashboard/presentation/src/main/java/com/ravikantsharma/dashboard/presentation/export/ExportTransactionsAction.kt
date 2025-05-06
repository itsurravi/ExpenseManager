package com.ravikantsharma.dashboard.presentation.export

import com.ravikantsharma.core.domain.export.model.ExportType

sealed interface ExportTransactionsAction {
    data class OnExportTypeUpdated(val exportType: ExportType) : ExportTransactionsAction
    data object OnExportClicked : ExportTransactionsAction
    data object OnDismissClicked : ExportTransactionsAction
}