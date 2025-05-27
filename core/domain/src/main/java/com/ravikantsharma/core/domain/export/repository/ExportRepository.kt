package com.ravikantsharma.core.domain.export.repository

import com.ravikantsharma.core.domain.export.model.ExportType
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result

interface ExportRepository {
    suspend fun exportTransactions(
        exportType: ExportType,
        userId: Long,
        userPreference: UserPreferences
    ): Result<Boolean, DataError>
}