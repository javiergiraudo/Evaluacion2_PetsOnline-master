package com.example.evaluacion2_petsonline.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.domain.model.Reserva
import com.example.evaluacion2_petsonline.data.local.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ReservaUiState(
    val lista: List<Reserva> = emptyList(),
    val nombreMascota: String = "",
    val servicio: String = "",
    val fecha: String = "",
    val observacion: String = "",
    val error: String? = null
)

class ReservaViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ReservaRepository(app)

    private val _ui = MutableStateFlow(ReservaUiState())
    val ui: StateFlow<ReservaUiState> = _ui

    init {
        viewModelScope.launch {
            repo.getReservas().collectLatest { reservas ->
                _ui.value = _ui.value.copy(lista = reservas)
            }
        }
    }

    fun onMascota(v: String) { _ui.value = _ui.value.copy(nombreMascota = v) }
    fun onServicio(v: String) { _ui.value = _ui.value.copy(servicio = v) }
    fun onFecha(v: String) { _ui.value = _ui.value.copy(fecha = v) }
    fun onObs(v: String) { _ui.value = _ui.value.copy(observacion = v) }

    private fun isValidDate(input: String): Boolean {
        if (!Regex("""^\d{2}/\d{2}/\d{4}$""").matches(input)) return false
        return try {
            java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).apply {
                isLenient = false
            }.parse(input)
            true
        } catch (_: Exception) { false }
    }

    fun agregarReserva() {
        val s = _ui.value
        if (s.nombreMascota.isBlank() || s.servicio.isBlank() || s.fecha.isBlank()) {
            _ui.value = s.copy(error = "Completa todos los campos obligatorios")
            return
        }
        if (!isValidDate(s.fecha)) {
            _ui.value = s.copy(error = "La fecha debe ser válida con formato dd/MM/yyyy")
            return
        }
        val nueva = Reserva(
            id = (s.lista.maxOfOrNull { it.id } ?: 0) + 1,
            nombreMascota = s.nombreMascota,
            servicio = s.servicio,
            fecha = s.fecha,
            observacion = s.observacion
        )
        viewModelScope.launch { repo.saveReserva(nueva) }
        _ui.value = s.copy(nombreMascota = "", servicio = "", fecha = "", observacion = "", error = null)
    }
    fun eliminarReserva(id: Int) {
        viewModelScope.launch { repo.deleteReserva(id) }
    }


}
