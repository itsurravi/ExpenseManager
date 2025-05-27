package com.ravikantsharma.core.database.transactions.utils

import androidx.room.TypeConverter
import com.ravikantsharma.core.domain.model.TransactionCategory
import com.ravikantsharma.core.domain.utils.toEpochMillis
import com.ravikantsharma.core.domain.utils.toLocalDateTime
import java.math.BigDecimal
import java.time.LocalDateTime

object TransactionConverters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return try {
            value?.let { BigDecimal(it) }
        } catch (e: NumberFormatException) {
            null
        }
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.toEpochMillis()
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.toLocalDateTime()
    }

    @TypeConverter
    fun fromTransactionCategory(category: TransactionCategory): String {
        return category.name
    }

    @TypeConverter
    fun toTransactionCategory(category: String): TransactionCategory {
        return TransactionCategory.valueOf(category)
    }
}