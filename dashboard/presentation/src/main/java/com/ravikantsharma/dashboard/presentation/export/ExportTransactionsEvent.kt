package com.ravikantsharma.dashboard.presentation.export

sealed interface ExportTransactionsEvent {
    data class ExportStatus(val isExportSuccess: Boolean) : ExportTransactionsEvent
    data object CloseBottomSheet : ExportTransactionsEvent
}