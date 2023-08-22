package com.raisproject.githubuser.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingDataStore private constructor(private val context: Context){

    private val THEME_KEY = booleanPreferencesKey("theme_setting")


    val Context.userPrefDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun getThemeSetting(): Flow<Boolean> {
        return context.userPrefDataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSettings(isDarkModeActive: Boolean) {
        context.userPrefDataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingDataStore? = null

        fun getInstance(context: Context): SettingDataStore {
            return  INSTANCE ?: synchronized(this) {
                val instance = SettingDataStore(context)
                INSTANCE = instance
                instance
            }
        }
    }
}