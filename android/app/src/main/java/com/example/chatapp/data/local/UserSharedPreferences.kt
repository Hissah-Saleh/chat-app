package com.example.chatapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSharedPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private companion object {
        val KEY_USERNAME = stringPreferencesKey("KEY_USERNAME")

    }

    suspend fun setUsername(username: String) {
        dataStore.edit {
            it[KEY_USERNAME] = username
        }

    }

    fun getCurrentUser(): Flow<String> {
        return dataStore.data.map { it[KEY_USERNAME] ?: "" }
    }

}