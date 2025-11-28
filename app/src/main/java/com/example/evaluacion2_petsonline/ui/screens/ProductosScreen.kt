package com.example.evaluacion2_petsonline.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.evaluacion2_petsonline.ui.viewmodels.ProductoViewModel

@Composable
fun ProductosScreen(navController: NavController, vm: ProductoViewModel = viewModel()) {
    val productos by vm.productos.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("home") }) {
                Text("🏠")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Productos Veterinarios", style = MaterialTheme.typography.headlineSmall)

            if (productos.isEmpty()) {
                Text("Cargando productos...")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(producto.descripcion)
                                Text("Precio: $${producto.precio}")
                            }
                        }
                    }
                }
            }
        }
    }
}
