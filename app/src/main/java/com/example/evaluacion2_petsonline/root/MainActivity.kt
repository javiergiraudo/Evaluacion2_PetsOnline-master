package com.example.evaluacion2_petsonline.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.ui.navigation.AppNavigation
import com.example.evaluacion2_petsonline.ui.theme.Evaluacion2_PetsOnlineTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasSession = runBlocking {
            val session = SessionManager(applicationContext)
            session.getToken() != null
        }

        setContent {
            Evaluacion2_PetsOnlineTheme {
                AppNavigation(startDestination = if (hasSession) "home" else "login")
            }
        }
    }
}