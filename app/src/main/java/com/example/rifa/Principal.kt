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

@Composable
fun PrincipalScreen(context: Context) {
    val db = remember(context) { BD(context) }
    var searchText by remember { mutableStateOf("") }
    var rifas by remember { mutableStateOf(db.obtenerRifas()) } // Carga inicial de las rifas
    val localContext = LocalContext.current


    val nuevoRifaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        rifas = db.obtenerRifas(searchText)
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
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nombre: ${rifa.nombre}", style = MaterialTheme.typography.titleMedium)
                        Text("Inscritos: ${rifa.inscritos.toString()}")
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