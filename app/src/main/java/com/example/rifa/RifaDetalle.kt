package com.example.rifadb

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class RifaDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: ""
        val inscritos = intent.getIntExtra("inscritos", 0)
        setContent {
            RifaDetalleScreen(nombre, fecha, inscritos)
        }
    }
}

@Composable
fun RifaDetalleScreen(nombre: String, fecha: String, inscritos: Int) {
    val context = LocalContext.current
    var boletoGanador by remember { mutableStateOf("") }
    var inhabilitar by remember { mutableStateOf(false) }
    val numerosDisponibles = List(100) { it }
    val numerosNoDisponibles = remember { numerosDisponibles.shuffled().take(inscritos) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Rifa: $nombre", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            modifier = Modifier.height(300.dp)
        ) {
            items(numerosDisponibles) { numero ->
                val estaInhabilitado = numero in numerosNoDisponibles
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(2.dp)
                        .background(if (estaInhabilitado) Color(0xFFE1BEE7) else Color.White)
                ) {
                    Text(numero.toString().padStart(2, '0'))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = boletoGanador,
            onValueChange = { boletoGanador = it },
            label = { Text("Boleto ganador") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Inhabilitar")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = inhabilitar, onCheckedChange = { inhabilitar = it })
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* Guardar lógica aquí */ }) {
                Text("Guardar")
            }
            Button(onClick = { /* Eliminar lógica aquí */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Eliminar")
            }
        }
    }
}
