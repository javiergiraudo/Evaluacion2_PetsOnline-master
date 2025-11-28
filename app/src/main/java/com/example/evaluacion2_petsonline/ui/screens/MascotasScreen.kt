package com.example.evaluacion2_petsonline.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import com.example.evaluacion2_petsonline.viewmodel.MascotaViewModel
import androidx.compose.material3.HorizontalDivider

@Composable
fun MascotasScreen(navController: NavController, vm: MascotaViewModel = viewModel()) {
    val state by vm.ui.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("home") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
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
            Text(
                text = "Registro de Mascotas",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { vm.onNombre(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.especie,
                onValueChange = { vm.onEspecie(it) },
                label = { Text("Especie") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.edad,
                onValueChange = { vm.onEdad(it) },
                label = { Text("Edad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { vm.onDescripcion(it) },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    vm.agregarMascota()
                    mensaje = vm.ui.value.error ?: "Mascota agregada ✅"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Mascota")
            }

            LaunchedEffect(mensaje) {
                mensaje?.let {
                    snackbarHostState.showSnackbar(it)
                    mensaje = null
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Lista de Mascotas",
                style = MaterialTheme.typography.headlineSmall
            )

            if (state.lista.isEmpty()) {
                Text(
                    text = "Aún no hay mascotas registradas 🐾",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(state.lista, key = { it.id }) { mascota ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        "${mascota.nombre} (${mascota.especie}) - ${mascota.edad} años",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    if (mascota.descripcion.isNotBlank()) {
                                        Text(mascota.descripcion)
                                    }
                                    Button(
                                        onClick = { vm.eliminarMascota(mascota.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        ),
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
}
