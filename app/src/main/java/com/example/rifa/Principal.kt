package com.example.rifadb

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun PrincipalScreen(context: Context) {
    val db = remember(context) { BD(context) }
    var searchText by remember { mutableStateOf("") }
    var rifas by remember { mutableStateOf(db.obtenerRifas()) } // Carga inicial de las rifas
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val nuevoRifaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        rifas = db.obtenerRifas(searchText)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                rifas = db.obtenerRifas(searchText) // <-- se recarga al volver
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Buscar rifa") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                rifas = db.obtenerRifas(searchText) // Filtrado al pulsar el botón
            }) {
                Text("Buscar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(rifas) { rifa ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            val intent = Intent(context, RifaDetalle::class.java).apply {
                                putExtra("nombre", rifa.nombre)
                                putExtra("fecha", rifa.fecha)
                                putExtra("inscritos", rifa.inscritos)
                            }
                            context.startActivity(intent)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nombre: ${rifa.nombre}", style = MaterialTheme.typography.titleMedium)
                        Text("Inscritos: ${rifa.inscritos}")
                        Text("Fecha: ${rifa.fecha}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(localContext, RifaNew::class.java)
                nuevoRifaLauncher.launch(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Rifa Nueva")
        }
    }
}