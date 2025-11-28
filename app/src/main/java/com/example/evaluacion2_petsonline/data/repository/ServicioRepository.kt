package com.example.evaluacion2_petsonline.data.local.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.evaluacion2_petsonline.domain.model.Servicio
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.servicioDataStore by preferencesDataStore("servicios_store")

class ServicioRepository(private val context: Context) {
    private val gson = Gson()
    private val KEY = stringPreferencesKey("servicios_list")

    fun getServicios(): Flow<List<Servicio>> {
        return context.servicioDataStore.data.map { prefs ->
            val json = prefs[KEY] ?: "[]"
            val type = object : TypeToken<List<Servicio>>() {}.type
            gson.fromJson(json, type)
        }
    }

    suspend fun inicializarServicios() {
        val actuales = getCurrentServicios()
        if (actuales.isEmpty()) {
            val serviciosIniciales = listOf(
                Servicio(1, "Consulta general", "Atención veterinaria básica", 15000.0),
                Servicio(2, "Vacunación", "Aplicación de vacunas según edad", 12000.0),
                Servicio(3, "Desparasitación", "Tratamiento interno y externo", 8000.0),
                Servicio(4, "Peluquería", "Baño y corte de pelo para mascotas", 10000.0)
            )
            saveList(serviciosIniciales)
        }
    }

    private suspend fun saveList(list: List<Servicio>) {
        val json = gson.toJson(list)
        context.servicioDataStore.edit { it[KEY] = json }
    }

    private suspend fun getCurrentServicios(): List<Servicio> {
        val prefs = context.servicioDataStore.data.map { it[KEY] ?: "[]" }.first()
        val type = object : TypeToken<List<Servicio>>() {}.type
        return gson.fromJson(prefs, type)
    }
}
