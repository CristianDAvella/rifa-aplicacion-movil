package com.example.rifa.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.rifa.view.ui.theme.RifaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RifaTheme {
                // Obtenemos el Context local dentro del contexto Composable
                Principal()
            }
        }
    }
}