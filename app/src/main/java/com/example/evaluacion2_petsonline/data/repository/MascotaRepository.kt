package com.example.evaluacion2_petsonline.data.local.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.evaluacion2_petsonline.domain.model.Mascota
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.mascotaDataStore by preferencesDataStore("mascotas_store")

class MascotaRepository(private val context: Context) {
    private val gson = Gson()
    private val KEY = stringPreferencesKey("mascotas_list")

    fun getMascotas(): Flow<List<Mascota>> {
        return context.mascotaDataStore.data.map { prefs ->
            val json = prefs[KEY] ?: "[]"
            val type = object : TypeToken<List<Mascota>>() {}.type
            gson.fromJson(json, type)
        }
    }

    suspend fun saveMascota(mascota: Mascota) {
        val current = getCurrentMascotas().toMutableList()
        current.add(mascota)
        saveList(current)
    }

    suspend fun deleteMascota(id: Int) {
        val updated = getCurrentMascotas().filter { it.id != id }
        saveList(updated)
    }

    private suspend fun saveList(list: List<Mascota>) {
        val json = gson.toJson(list)
        context.mascotaDataStore.edit { prefs -> prefs[KEY] = json }
    }

    suspend fun getCurrentMascotas(): List<Mascota> {
        val json = context.mascotaDataStore.data.map { it[KEY] ?: "[]" }.first()
        val type = object : TypeToken<List<Mascota>>() {}.type
        return gson.fromJson(json, type)
    }
}
