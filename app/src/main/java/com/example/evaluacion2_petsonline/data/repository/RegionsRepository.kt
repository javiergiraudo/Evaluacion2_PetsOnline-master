package com.example.evaluacion2_petsonline.data.repository

import android.content.Context
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.data.remote.ApiService
import com.example.evaluacion2_petsonline.data.remote.RegionResponse
import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RegionsRepository(context: Context) {
    private val api = RetrofitClient.create(context).create(ApiService::class.java)
    private val session = SessionManager(context)
    private val gson = Gson()

    suspend fun getRegions(): Result<List<RegionResponse>> {
        return try {
            val list = api.getRegions()
            // cache JSON
            try {
                val json = gson.toJson(list)
                session.saveRegionsJson(json)
            } catch (_: Exception) {
            }
            Result.success(list)
        } catch (e: Exception) {
            // on error try cached
            val cached = try { session.getRegionsJson() } catch (_: Exception) { null }
            if (!cached.isNullOrBlank()) {
                return try {
                    val type = object : TypeToken<List<RegionResponse>>() {}.type
                    val list: List<RegionResponse> = gson.fromJson(cached, type)
                    Result.success(list)
                } catch (ex: Exception) {
                    Result.failure(Exception("Error al parsear regiones en caché"))
                }
            }

            val msg = when (e) {
                is HttpException -> "Error del servidor (${e.code()})."
                is UnknownHostException -> "No se pudo conectar al servidor. Verifica tu conexión a Internet."
                is SocketTimeoutException -> "La conexión tardó demasiado. Intenta nuevamente."
                is IOException -> "Error de entrada/salida: ${e.localizedMessage ?: ""}"
                else -> "Ocurrió un error inesperado al obtener las regiones."
            }
            Result.failure(Exception(msg))
        }
    }
}
