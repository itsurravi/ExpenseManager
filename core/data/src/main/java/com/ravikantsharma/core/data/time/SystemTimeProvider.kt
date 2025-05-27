package com.ravikantsharma.core.data.time

import com.ravikantsharma.core.domain.time.TimeProvider
import java.time.LocalDateTime
import java.time.ZoneId

class SystemTimeProvider(
    private val zoneId: ZoneId
) : TimeProvider {
    override val currentLocalDateTime: LocalDateTime
        get() = LocalDateTime.now(zoneId)
}