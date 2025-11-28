package com.example.evaluacion2_petsonline.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.domain.model.Mascota
import com.example.evaluacion2_petsonline.data.local.repository.MascotaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MascotaUiState(
    val lista: List<Mascota> = emptyList(),
    val nombre: String = "",
    val especie: String = "",
    val edad: String = "",
    val descripcion: String = "",
    val error: String? = null
)

class MascotaViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MascotaRepository(app)

    private val _ui = MutableStateFlow(MascotaUiState())
    val ui: StateFlow<MascotaUiState> = _ui

    init {
        viewModelScope.launch {
            val savedMascotas = repo.getCurrentMascotas()
            _ui.value = _ui.value.copy(lista = savedMascotas)

            repo.getMascotas().collect { mascotas ->
                _ui.value = _ui.value.copy(lista = mascotas)
            }
        }
    }

    fun onNombre(v: String) { _ui.value = _ui.value.copy(nombre = v) }
    fun onEspecie(v: String) { _ui.value = _ui.value.copy(especie = v) }
    fun onEdad(v: String) { _ui.value = _ui.value.copy(edad = v) }
    fun onDescripcion(v: String) { _ui.value = _ui.value.copy(descripcion = v) }

    fun agregarMascota() {
        val s = _ui.value
        if (s.nombre.isBlank() || s.especie.isBlank() || s.edad.isBlank()) {
            _ui.value = s.copy(error = "Completa todos los campos obligatorios")
            return
        }
        val mascota = Mascota(
            id = (s.lista.maxOfOrNull { it.id } ?: 0) + 1,
            nombre = s.nombre,
            especie = s.especie,
            edad = s.edad.toIntOrNull() ?: 0,
            descripcion = s.descripcion
        )
        viewModelScope.launch { repo.saveMascota(mascota) }
        _ui.value = s.copy(nombre = "", especie = "", edad = "", descripcion = "", error = null)
    }

    fun eliminarMascota(id: Int) {
        viewModelScope.launch { repo.deleteMascota(id) }
    }
}
