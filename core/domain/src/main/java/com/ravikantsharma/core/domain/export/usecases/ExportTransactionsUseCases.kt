package com.ravikantsharma.core.domain.export.usecases

import com.ravikantsharma.core.domain.export.model.ExportType
import com.ravikantsharma.core.domain.export.repository.ExportRepository
import com.ravikantsharma.core.domain.preference.repository.UserPreferencesRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

data class ExportTransactionsUseCases(
    val exportTransactionUseCase: ExportTransactionUseCase
)

class ExportTransactionUseCase(
    private val exportRepository: ExportRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(userId: Long, exportType: ExportType): Result<Boolean, DataError> {
        return try {
            val preferencesResult = userPreferencesRepository.getPreferences(userId)
                .catch { emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)) }
                .first()

            when (preferencesResult) {
                is Result.Success -> {
                    exportRepository.exportTransactions(
                        dateRange = exportType,
                        userId = userId,
                        userPreference = preferencesResult.data
                    )
                }

                is Result.Error -> Result.Error(preferencesResult.error)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }
}
