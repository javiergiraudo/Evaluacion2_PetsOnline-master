package com.example.evaluacion2_petsonline.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.local.repository.ProductoRepository
import com.example.evaluacion2_petsonline.domain.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ProductoRepository(app)

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        viewModelScope.launch {
            repo.inicializarProductos()
            repo.getProductos().collect {
                _productos.value = it
            }
        }
    }
}