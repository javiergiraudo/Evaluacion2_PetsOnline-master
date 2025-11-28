package com.example.evaluacion2_petsonline.data.local.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.evaluacion2_petsonline.domain.model.Reserva
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.reservaDataStore by preferencesDataStore("reservas_store")

class ReservaRepository(private val context: Context) {
    private val gson = Gson()
    private val KEY = stringPreferencesKey("reservas_list")

    fun getReservas(): Flow<List<Reserva>> {
        return context.reservaDataStore.data.map { prefs ->
            val json = prefs[KEY] ?: "[]"
            val type = object : TypeToken<List<Reserva>>() {}.type
            gson.fromJson(json, type)
        }
    }

    suspend fun saveReserva(reserva: Reserva) {
        val list = getCurrentReservas().toMutableList()
        list.add(reserva)
        saveList(list)
    }

    suspend fun deleteReserva(id: Int) {
        val list = getCurrentReservas().filter { it.id != id }
        saveList(list)
    }

    private suspend fun saveList(list: List<Reserva>) {
        val json = gson.toJson(list)
        context.reservaDataStore.edit { it[KEY] = json }
    }

    private suspend fun getCurrentReservas(): List<Reserva> {
        val prefs = context.reservaDataStore.data.map { it[KEY] ?: "[]" }.first()
        val type = object : TypeToken<List<Reserva>>() {}.type
        return gson.fromJson(prefs, type)
    }
}
