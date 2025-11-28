package com.example.evaluacion2_petsonline.data.repository

import android.content.Context
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.data.remote.ApiService
import com.example.evaluacion2_petsonline.data.remote.LoginRequest
import com.example.evaluacion2_petsonline.data.remote.RetrofitClient
import retrofit2.HttpException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AuthRepository(context: Context) {
    private val api = RetrofitClient.create(context).create(ApiService::class.java)
    private val session = SessionManager(context)

    suspend fun saveToken(token: String) {
        session.saveToken(token)
    }

    suspend fun getToken(): String? {
        return session.getToken()
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, password))
            val token = response.authToken ?: return Result.failure(Exception("No se recibió token"))
            saveToken(token)
            Result.success(token)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val msg = when (e.code()) {
                        400 -> "Datos inválidos. Verifica tu correo y contraseña."
                        401, 403 -> "Correo o contraseña incorrectos."
                        else -> "Error del servidor (${e.code()}). Intenta nuevamente."
                    }
                    Result.failure(Exception(msg))
                }
                is UnknownHostException -> {
                    Result.failure(Exception("No se pudo conectar al servidor. Verifica tu conexión a Internet."))
                }
                is SocketTimeoutException -> {
                    Result.failure(Exception("La conexión tardó demasiado. Intenta nuevamente."))
                }
                else -> {
                    Result.failure(Exception("Ocurrió un error inesperado: ${e.localizedMessage ?: "Intenta más tarde."}"))
                }
            }
        }
    }

    suspend fun signup(email: String, password: String, photoPath: String? = null): Result<String> {
        return try {
            val response = if (photoPath.isNullOrBlank()) {
                api.signup(LoginRequest(email, password))
            } else {
                // Enviar multipart: email + password + photo en una sola llamada
                val file = java.io.File(photoPath)
                val reqEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val reqPass = password.toRequestBody("text/plain".toMediaTypeOrNull())
                val reqFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = okhttp3.MultipartBody.Part.createFormData("photo", file.name, reqFile)
                api.signupWithPhoto(reqEmail, reqPass, body)
            }
            val token = response.authToken ?: return Result.failure(Exception("No se recibió token"))
            saveToken(token)
            Result.success(token)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val msg = when (e.code()) {
                        400 -> "Datos inválidos. Verifica que el correo y la contraseña cumplan los requisitos."
                        403 -> "Esta cuenta ya está registrada."
                        409 -> "El usuario ya existe en el sistema."
                        else -> "Error del servidor (${e.code()}). Intenta nuevamente."
                    }
                    Result.failure(Exception(msg))
                }
                is UnknownHostException -> {
                    Result.failure(Exception("No se pudo conectar al servidor. Verifica tu conexión a Internet."))
                }
                is SocketTimeoutException -> {
                    Result.failure(Exception("La conexión tardó demasiado. Intenta nuevamente."))
                }
                else -> {
                    Result.failure(Exception("Ocurrió un error inesperado: ${e.localizedMessage ?: "Intenta más tarde."}"))
                }
            }
        }
    }

    suspend fun getProfile(): Result<String> {
        return try {
            val token = session.getToken() ?: return Result.failure(Exception("No hay token guardado"))
            val profile = api.getProfile("Bearer $token")
            val email = profile["email"]?.toString() ?: "Sin correo"
            Result.success(email)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val msg = when (e.code()) {
                        401, 403 -> "Sesión expirada. Inicia sesión nuevamente."
                        else -> "Error al obtener el perfil (${e.code()})."
                    }
                    Result.failure(Exception(msg))
                }
                is UnknownHostException -> {
                    Result.failure(Exception("No se pudo conectar al servidor. Verifica tu conexión a Internet."))
                }
                is SocketTimeoutException -> {
                    Result.failure(Exception("La conexión tardó demasiado. Intenta nuevamente."))
                }
                else -> {
                    Result.failure(Exception("Ocurrió un error inesperado al obtener el perfil."))
                }
            }
        }
    }
}
