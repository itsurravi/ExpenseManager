package com.ravikantsharma.core.domain.utils

import java.time.LocalDateTime
import java.time.ZoneId

object CalendarUtils {
    private val zoneId: ZoneId = ZoneId.of("Asia/Kolkata")

    val currentEstTime: LocalDateTime
        get() = LocalDateTime.now(zoneId)

    fun toEpochMillis(localDateTime: LocalDateTime): Long {
        return localDateTime.atZone(zoneId).toInstant().toEpochMilli()
    }
}