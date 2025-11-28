package com.example.evaluacion2_petsonline.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.local.repository.ServicioRepository
import com.example.evaluacion2_petsonline.domain.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServicioViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ServicioRepository(app)

    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    init {
        viewModelScope.launch {
            repo.inicializarServicios()
            repo.getServicios().collect {
                _servicios.value = it
            }
        }
    }
}