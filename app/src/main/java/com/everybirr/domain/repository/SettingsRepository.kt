package com.everybirr.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val darkMode: Flow<Boolean>
    val language: Flow<String>
    val securityPinEnabled: Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setLanguage(languageCode: String)
    suspend fun setSecurityPinEnabled(enabled: Boolean)
}
