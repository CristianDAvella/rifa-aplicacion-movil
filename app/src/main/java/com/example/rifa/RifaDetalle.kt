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
        setContent {
            RifaDetalleScreen(nombre, fecha, this)
        }
    }
}

@Composable
fun RifaDetalleScreen(nombre: String, fecha: String, context: Context) {
    val db = remember(context) { BD(context) }
    val numeros = remember { mutableStateListOf<Pair<Int, Boolean>>() }

    LaunchedEffect(Unit) {
        numeros.clear()
        numeros.addAll(db.obtenerNumeros(nombre).sortedBy { it.first })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Rifa: $nombre", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Fecha: $fecha")
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(numeros) { (numero, comprado) ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(if (comprado) Color.Gray else Color.White)
                        .clickable {
                            val idx = numeros.indexOfFirst { it.first == numero }
                            if (idx != -1) {
                                numeros[idx] = numero to !comprado
                            }
                        }
                ) {
                    Text(
                        numero.toString().padStart(2, '0'),
                        color = if (comprado) Color.White else Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val seleccionados = numeros.filter { it.second }.map { it.first }.toSet()
                db.actualizarNumeros(nombre, seleccionados)
                (context as? ComponentActivity)?.apply {
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }) {
                Text("Guardar")
            }

            Button(onClick = {
                db.eliminarRifa(nombre)
                (context as? ComponentActivity)?.apply {
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Eliminar")
            }
        }
    }
}

