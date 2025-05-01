package com.ravikantsharma.session_management.data.utils

import com.ravikantsharma.session_management.data.SessionPreferences
import com.ravikantsharma.session_management.domain.model.SessionData

fun SessionPreferences.toDomain(): SessionData {
    return SessionData(
        userId = this.userId,
        userName = this.userName,
        sessionExpiryTime = this.sessionExpiryTime
    )
}

fun SessionData.toProto(): SessionPreferences {
    return SessionPreferences.newBuilder()
        .setUserId(this.userId)
        .setUserName(this.userName)
        .setSessionExpiryTime(this.sessionExpiryTime)
        .build()
}