package com.example.evaluacion2_petsonline.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.evaluacion2_petsonline.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.login() },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "Cargando..." else "Ingresar")
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            if (state.success) {
                LaunchedEffect(Unit) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }
    }
}

