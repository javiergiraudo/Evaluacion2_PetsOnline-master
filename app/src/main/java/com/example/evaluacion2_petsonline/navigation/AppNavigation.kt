package com.example.evaluacion2_petsonline.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val session = remember { SessionManager(context) }

    var startDestination by remember { mutableStateOf("splash") }

    LaunchedEffect(Unit) {
        startDestination = if (session.getToken() != null) "home" else "login"
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("PetsOnline SPA") }) },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController, session) }
            composable("login") { LoginScreen(navController) }
            composable("signup") { SignupScreen(navController) }
            composable("home") { HomeScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
            composable("mascotas") { MascotasScreen(navController) }
            composable("productos") { ProductosScreen(navController) }
            composable("servicios") { ServiciosScreen(navController) }
            composable("reserva") { ReservaScreen(navController) }
        }
    }
}
