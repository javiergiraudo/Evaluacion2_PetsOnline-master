package com.example.evaluacion2_petsonline.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.remote.RegionResponse
import com.example.evaluacion2_petsonline.data.repository.AuthRepository
import com.example.evaluacion2_petsonline.data.repository.RegionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val regions: List<RegionResponse> = emptyList(),
    val isRegionsLoading: Boolean = false,
    val regionsError: String? = null,
    val selectedRegionCode: String? = null,
    val selectedRegionName: String? = null
    ,
    val photoPath: String? = null
)

class SignupViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)
    private val regionsRepository = RegionsRepository(application)

    private val _ui = MutableStateFlow(SignupUiState())
    val ui: StateFlow<SignupUiState> = _ui

    fun onEmail(value: String) = _ui.value.run { _ui.value = copy(email = value) }
    fun onPassword(value: String) = _ui.value.run { _ui.value = copy(password = value) }
    fun onConfirmPassword(value: String) = _ui.value.run { _ui.value = copy(confirmPassword = value) }

    fun onRegionSelected(code: String?, name: String?) = _ui.value.run {
        _ui.value = copy(selectedRegionCode = code, selectedRegionName = name)
    }

    fun onPhotoTaken(path: String?) = _ui.value.run {
        _ui.value = copy(photoPath = path)
    }

    init {
        // Cargar regiones al inicializar el ViewModel
        viewModelScope.launch {
            val current = _ui.value
            _ui.value = current.copy(isRegionsLoading = true, regionsError = null)
            val result = regionsRepository.getRegions()
            _ui.value = result.fold(onSuccess = { list ->
                current.copy(regions = list, isRegionsLoading = false)
            }, onFailure = { err ->
                current.copy(isRegionsLoading = false, regionsError = err.localizedMessage ?: "Error al obtener regiones")
            })
        }
    }

    fun signup() {
        val s = _ui.value

        when {
            s.email.isBlank() || s.password.isBlank() || s.confirmPassword.isBlank() ->
                _ui.value = s.copy(error = "Todos los campos son obligatorios")

            !android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches() ->
                _ui.value = s.copy(error = "Correo electrónico inválido")

            s.password.length < 8 ->
                _ui.value = s.copy(error = "La contraseña debe tener al menos 8 caracteres")

            s.selectedRegionCode.isNullOrBlank() ->
                _ui.value = s.copy(error = "Seleccione una región")

            s.photoPath.isNullOrBlank() ->
                _ui.value = s.copy(error = "Por favor tome una foto del carnet")

            !s.password.any { it.isDigit() } ->
                _ui.value = s.copy(error = "La contraseña debe incluir al menos un número")

            s.password != s.confirmPassword ->
                _ui.value = s.copy(error = "Las contraseñas no coinciden")

            else -> viewModelScope.launch {
                _ui.value = s.copy(isLoading = true, error = null)

                try {
                    val result = repository.signup(s.email, s.password, s.photoPath)
                    _ui.value = result.fold(
                        onSuccess = { s.copy(isLoading = false, success = true) },
                        onFailure = { throwable ->
                            val userFriendly = when (throwable) {
                                is HttpException -> when (throwable.code()) {
                                    400 -> "Datos inválidos. Verifica tu correo y contraseña."
                                    403 -> "Esta cuenta ya está registrada."
                                    else -> "Error del servidor (${throwable.code()})."
                                }
                                is IOException -> "No hay conexión a internet. Intenta nuevamente."
                                else -> "Ocurrió un error inesperado: ${throwable.localizedMessage}"
                            }
                            s.copy(isLoading = false, error = userFriendly)
                        }
                    )
                } catch (e: Exception) {
                    val message = when (e) {
                        is HttpException -> when (e.code()) {
                            400 -> "Datos inválidos. Verifica tu correo y contraseña."
                            403 -> "Esta cuenta ya está registrada."
                            else -> "Error del servidor (${e.code()})."
                        }
                        is IOException -> "No hay conexión a internet. Intenta nuevamente."
                        else -> "Ocurrió un error inesperado: ${e.localizedMessage}"
                    }
                    _ui.value = s.copy(isLoading = false, error = message)
                }
            }
        }
    }
}
