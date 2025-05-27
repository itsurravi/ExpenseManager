package com.ravikantsharma.core.domain.time

import java.time.LocalDateTime

interface TimeProvider {
    val currentLocalDateTime: LocalDateTime
}