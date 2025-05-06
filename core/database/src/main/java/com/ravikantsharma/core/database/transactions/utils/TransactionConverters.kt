package com.ravikantsharma.core.database.transactions.utils

import androidx.room.TypeConverter
import com.ravikantsharma.core.domain.model.TransactionCategory
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object TransactionConverters {
    private val zoneId = ZoneId.of("Asia/Kolkata")

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
        return value?.atZone(zoneId)?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it).atZone(zoneId).toLocalDateTime()
        }
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