package com.example.evaluacion2_petsonline.data.local.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.evaluacion2_petsonline.domain.model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.productoDataStore by preferencesDataStore("productos_store")

class ProductoRepository(private val context: Context) {
    private val gson = Gson()
    private val KEY = stringPreferencesKey("productos_list")

    fun getProductos(): Flow<List<Producto>> {
        return context.productoDataStore.data.map { prefs ->
            val json = prefs[KEY] ?: "[]"
            val type = object : TypeToken<List<Producto>>() {}.type
            gson.fromJson(json, type)
        }
    }

    suspend fun inicializarProductos() {
        val actuales = getCurrentProductos()
        if (actuales.isEmpty()) {
            val productosIniciales = listOf(
                Producto(1, "Alimento Premium", "Bolsa de 5kg para perros adultos", 22000.0),
                Producto(2, "Collar antipulgas", "Protección por 6 meses", 9500.0),
                Producto(3, "Juguete masticable", "Ideal para cachorros", 4500.0),
                Producto(4, "Shampoo hipoalergénico", "Especial para piel sensible", 7800.0)
            )
            saveList(productosIniciales)
        }
    }

    private suspend fun saveList(list: List<Producto>) {
        val json = gson.toJson(list)
        context.productoDataStore.edit { it[KEY] = json }
    }

    private suspend fun getCurrentProductos(): List<Producto> {
        val prefs = context.productoDataStore.data.map { it[KEY] ?: "[]" }.first()
        val type = object : TypeToken<List<Producto>>() {}.type
        return gson.fromJson(prefs, type)
    }
}
