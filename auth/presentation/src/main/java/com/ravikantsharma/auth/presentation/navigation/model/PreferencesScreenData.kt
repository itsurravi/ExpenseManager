package com.ravikantsharma.auth.presentation.navigation.model

import kotlinx.serialization.Serializable

@Serializable
data class PreferencesScreenData(
    val username: String,
    val pin: String? = null
)
