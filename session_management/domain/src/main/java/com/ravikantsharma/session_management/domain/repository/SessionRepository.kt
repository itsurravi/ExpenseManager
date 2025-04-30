package com.ravikantsharma.session_management.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun startSession()
    suspend fun clearSession()
    fun isSessionExpired(): Flow<Boolean>
    suspend fun checkAndUpdateSessionExpiry()
}