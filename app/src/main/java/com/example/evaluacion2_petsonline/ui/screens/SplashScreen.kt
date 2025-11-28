package com.example.evaluacion2_petsonline.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.evaluacion2_petsonline.R
import com.example.evaluacion2_petsonline.data.local.SessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, session: SessionManager) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 2000)
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alpha)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "PetsOnline SPA",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}
