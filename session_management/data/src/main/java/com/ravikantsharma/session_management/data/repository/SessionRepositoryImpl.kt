package com.ravikantsharma.session_management.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.ravikantsharma.session_management.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

class SessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    override suspend fun startSession() {
        val durationMinutes: Long = 1
        val expirationTime = System.currentTimeMillis() + (durationMinutes * MINUTES_TO_MILLIS)

        dataStore.edit { preferences ->
            preferences[SESSION_EXPIRATION_TIME] = expirationTime
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences[SESSION_EXPIRATION_TIME] = 0L
        }
    }

    override fun isSessionExpired(): Flow<Boolean> {
        return getRemainingSessionTime().map { remainingTimeMs ->
            val isExpired = remainingTimeMs <= 0

            isExpired
        }
    }

    override suspend fun checkAndUpdateSessionExpiry() {
        if (isSessionExpired().first()) {
            clearSession()
        }
    }

    private fun getRemainingSessionTime(): Flow<Long> {
        return dataStore.data.map { preferences ->
            val expirationTime = preferences[SESSION_EXPIRATION_TIME] ?: 0L
            val remainingTimeMs = max(0, expirationTime - System.currentTimeMillis())

            remainingTimeMs
        }
    }

    companion object {
        private val SESSION_EXPIRATION_TIME = longPreferencesKey("session_expiration_time")
        private const val MINUTES_TO_MILLIS = 60 * 1000L
        private const val TAG = "SessionRepositoryImpl"

        private val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.US)

        fun formatTime(timestamp: Long): String {
            return if (timestamp > 0) dateFormat.format(Date(timestamp)) else "N/A"
        }
    }
}