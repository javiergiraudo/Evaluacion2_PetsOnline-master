package com.example.evaluacion2_petsonline.data.local.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.avatarDataStore by preferencesDataStore("avatar_store")

class AvatarRepository(private val context: Context) {
    companion object {
        private val AVATAR_KEY = stringPreferencesKey("avatar_uri")
    }

    suspend fun saveAvatar(uri: String) {
        context.avatarDataStore.edit { prefs ->
            prefs[AVATAR_KEY] = uri
        }
    }

    fun getAvatar(): Flow<String?> {
        return context.avatarDataStore.data.map { prefs ->
            prefs[AVATAR_KEY]
        }
    }

    suspend fun clearAvatar() {
        context.avatarDataStore.edit { prefs ->
            prefs.remove(AVATAR_KEY)
        }
    }
}
