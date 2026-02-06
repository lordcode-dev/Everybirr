package com.everybirr.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.everybirr.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    private companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val PIN_ENABLED = booleanPreferencesKey("pin_enabled")
    }

    override val darkMode: Flow<Boolean> = dataStore.data.map { it[DARK_MODE] ?: false }
    override val language: Flow<String> = dataStore.data.map { it[LANGUAGE] ?: "en" }
    override val securityPinEnabled: Flow<Boolean> = dataStore.data.map { it[PIN_ENABLED] ?: false }

    override suspend fun setDarkMode(enabled: Boolean) { dataStore.edit { it[DARK_MODE] = enabled } }
    override suspend fun setLanguage(languageCode: String) { dataStore.edit { it[LANGUAGE] = languageCode } }
    override suspend fun setSecurityPinEnabled(enabled: Boolean) { dataStore.edit { it[PIN_ENABLED] = enabled } }
}
