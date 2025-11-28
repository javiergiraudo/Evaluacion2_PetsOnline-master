package com.example.evaluacion2_petsonline.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("authToken")
    val authToken: String?,
    @SerializedName("user")
    val user: UserResponse?
)

data class UserResponse(
    val id: Int?,
    val email: String?
)

data class RegionResponse(
    val codigo: String?,
    val nombre: String?
)

interface ApiService {

    @POST("auth/signup")
    suspend fun signup(@Body request: LoginRequest): LoginResponse

    @Multipart
    @POST("auth/signup")
    suspend fun signupWithPhoto(
        @Part("email") email: okhttp3.RequestBody,
        @Part("password") password: okhttp3.RequestBody,
        @Part photo: okhttp3.MultipartBody.Part
    ): LoginResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getProfile(@Header("Authorization") token: String): Map<String, Any>

    // Regiones de Chile (API pública del gobierno)
    @GET("https://apis.digital.gob.cl/dpa/regiones")
    suspend fun getRegions(): List<RegionResponse>
}
