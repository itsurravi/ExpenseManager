package com.ravikantsharma.session_management.data.repository

import androidx.datastore.core.DataStore
import com.ravikantsharma.core.domain.preference.repository.UserPreferencesRepository
import com.ravikantsharma.core.domain.utils.Result
import com.ravikantsharma.session_management.data.SessionPreferences
import com.ravikantsharma.session_management.data.utils.toDomain
import com.ravikantsharma.session_management.data.utils.toProto
import com.ravikantsharma.session_management.domain.model.SessionData
import com.ravikantsharma.session_management.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionRepositoryImpl(
    private val dataStore: DataStore<SessionPreferences>,
    private val preferencesRepository: UserPreferencesRepository
) : SessionRepository {

    companion object {
        private const val MINUTES_TO_MILLIS = 1000L
        private const val TAG = "SessionRepository"

        // Date formatter for readable logs
        private val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.US)

        private fun formatTime(timestamp: Long): String {
            return if (timestamp > 0) dateFormat.format(Date(timestamp)) else "N/A"
        }
    }

    override suspend fun saveSession(sessionData: SessionData) {
        val userPreferences = preferencesRepository.getPreferences(sessionData.userId).first()
        when (userPreferences) {
            is Result.Success -> {
                val expirationTime =
                    System.currentTimeMillis() + (userPreferences.data.sessionDuration.getValueInLong() * MINUTES_TO_MILLIS)

                dataStore.updateData { prefs ->
                    sessionData.copy(sessionExpiryTime = expirationTime).toProto()
                }
            }

            is Result.Error -> Unit
        }
    }

    override suspend fun clearSession() {
        // ✅ Reset session data to default
        dataStore.updateData {
            SessionPreferences.getDefaultInstance()
        }
    }

    private suspend fun setSessionToExpired() {
        dataStore.updateData { prefs ->
            prefs.toBuilder().setSessionExpiryTime(0L).build()
        }
    }

    override fun getSessionData(): Flow<SessionData> {
        return dataStore.data.map { prefs ->
            prefs.toDomain()
        }
    }

    override fun isSessionExpired(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            val isExpired = System.currentTimeMillis() >= prefs.sessionExpiryTime
            if (isExpired) {
                setSessionToExpired()
            }
            isExpired
        }
    }

    override suspend fun checkAndUpdateSessionExpiry(): Boolean {
        val isSessionExpired = isSessionExpired().first()
        if (isSessionExpired) {
            setSessionToExpired()
        }
        return isSessionExpired
    }

    override suspend fun resetSessionExpiry() {
        dataStore.updateData { prefs ->
            val userPreference = preferencesRepository.getPreferences(prefs.userId).first()
            when (userPreference) {
                is Result.Success -> {
                    val sessionExpiryDurationMins = userPreference.data.sessionDuration.getValueInLong()
                    val newExpirationTime = System.currentTimeMillis() + (sessionExpiryDurationMins * MINUTES_TO_MILLIS)

                    prefs.toBuilder()
                        .setSessionExpiryTime(newExpirationTime)
                        .build()
                }

                is Result.Error -> prefs
            }

        }
    }
}