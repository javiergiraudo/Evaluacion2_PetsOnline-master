    package com.example.evaluacion2_petsonline.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.evaluacion2_petsonline.utils.NotificationHelper

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    var permisoConcedido by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { permitido -> permisoConcedido = permitido }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permisoConcedido = true
        } else {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(permisoConcedido) {
        if (permisoConcedido) {
            NotificationHelper.showNotification(
                context,
                "PetsOnline 🐾",
                "¡Recuerda revisar tus reservas veterinarias de hoy!"
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Pantalla Home",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { navController.navigate("profile") }) {
                Text("Ver Perfil")
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = { navController.navigate("mascotas") }) {
                Text("Mis Mascotas")
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = { navController.navigate("servicios") }) {
                Text("Servicios Veterinarios")
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = { navController.navigate("productos") }) {
                Text("Productos")
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = { navController.navigate("reserva") }) {
                Text("Reservar Cita")
            }
        }
    }
}
