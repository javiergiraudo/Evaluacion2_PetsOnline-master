package com.example.evaluacion2_petsonline.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.evaluacion2_petsonline.viewmodel.ReservaViewModel

@Composable
fun ReservaScreen(navController: NavController, vm: ReservaViewModel = viewModel()) {
    val state by vm.ui.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("home") },
                containerColor = MaterialTheme.colorScheme.primary
            ) { Text("🏠") }
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
            Text("Reservas Veterinarias", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = state.nombreMascota,
                onValueChange = { vm.onMascota(it) },
                label = { Text("Nombre de la mascota") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.servicio,
                onValueChange = { vm.onServicio(it) },
                label = { Text("Servicio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.fecha,
                onValueChange = { vm.onFecha(it.take(10)) },
                label = { Text("Fecha (dd/MM/yyyy)") },
                placeholder = { Text("24/01/2006") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.error?.contains("fecha", ignoreCase = true) == true
            )

            OutlinedTextField(
                value = state.observacion,
                onValueChange = { vm.onObs(it) },
                label = { Text("Observación (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    vm.agregarReserva()
                    mensaje = vm.ui.value.error ?: "Reserva registrada ✅"
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar Reserva") }

            LaunchedEffect(mensaje) {
                mensaje?.let {
                    snackbarHostState.showSnackbar(it)
                    mensaje = null
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Lista de Reservas", style = MaterialTheme.typography.headlineSmall)

            if (state.lista.isEmpty()) {
                Text("Aún no hay reservas registradas 🐾")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(state.lista) { reserva ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("${reserva.nombreMascota} - ${reserva.servicio}")
                                Text("Fecha: ${reserva.fecha}")
                                if (reserva.observacion.isNotBlank())
                                    Text("Obs: ${reserva.observacion}")
                                Button(
                                    onClick = { vm.eliminarReserva(reserva.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
