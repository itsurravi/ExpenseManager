package com.ravikantsharma.core.data.export.repository

import android.os.Environment
import com.ravikantsharma.core.domain.export.model.ExportType
import com.ravikantsharma.core.domain.export.repository.ExportRepository
import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.preference.model.UserPreferences
import com.ravikantsharma.core.domain.transactions.model.Transaction
import com.ravikantsharma.core.domain.transactions.repository.TransactionRepository
import com.ravikantsharma.core.domain.utils.DataError
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.core.domain.utils.toISODateString
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ExportRepositoryImpl(
    private val transactionRepository: TransactionRepository
) : ExportRepository {
    override suspend fun exportTransactions(
        dateRange: ExportType,
        userId: Long,
        userPreference: UserPreferences
    ): Result<Boolean, DataError> {
        val (startDate, endDate) = dateRange.getDateRange()

        return try {
            val transactionsResult = transactionRepository.getTransactionsForDateRange(userId, startDate, endDate)

            when (transactionsResult) {
                is Result.Success -> {
                    val exportResult =
                        writeTransactionsToCsv(transactionsResult.data, userPreference)
                    if (exportResult) {
                        Result.Success(true)
                    } else {
                        Result.Error(DataError.Local.EXPORT_FAILED)
                    }
                }

                is Result.Error -> Result.Error(transactionsResult.error)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    private fun writeTransactionsToCsv(
        transactions: List<Transaction>,
        userPreference: UserPreferences
    ): Boolean {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val csvFile = File(downloadsDir, "ExpenseManager_transactions.csv")

        return try {
            FileWriter(csvFile).use { writer ->
                val headers = listOf(
                    "Date",
                    "Transaction Type",
                    "Transaction Name",
                    "Amount",
                    "Transaction Category",
                    "Note"
                )
                writer.appendLine(headers.joinToString(",") { escapeCsv(it) })

                transactions.forEach { transaction ->
                    val csvLine = buildCsvLine(transaction, userPreference)
                    writer.appendLine(csvLine)
                }
                writer.flush()
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    private fun buildCsvLine(transaction: Transaction, userPreference: UserPreferences): String {
        return listOf(
            transaction.transactionDate.toISODateString(),
            transaction.transactionType.displayName,
            transaction.transactionName,
            NumberFormatter.formatAmount(
                amount = transaction.amount,
                preferences = userPreference
            ),
            transaction.transactionCategory.displayName,
            transaction.note.orEmpty()
        ).joinToString(",") { escapeCsv(it) }
    }

    private fun escapeCsv(value: String): String {
        val escapedValue = value.replace("\"", "\"\"")
        return "\"$escapedValue\""
    }
}