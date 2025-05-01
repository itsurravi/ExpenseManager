package com.ravikantsharma.session_management.domain.model

data class SessionData(
    val userId: Long,
    val userName: String,
    val sessionExpiryTime: Long
)