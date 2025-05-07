package com.example.rifadb

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class RifaNew : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewRifaScreen(this)
        }
    }
}

@Composable
fun NewRifaScreen(context: Context) {
    val db = remember(context) { BD(context) }
    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val contextCompose = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context,
        { _, yearSelected, monthOfYear, dayOfMonth ->
            val calendarSelected = Calendar.getInstance()
            calendarSelected.set(yearSelected, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            fecha = dateFormat.format(calendarSelected.time)
        },
        year,
        month,
        day
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear nueva Rifa", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la Rifa") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = fecha,
            onValueChange = { /* No se permite la edición manual, se selecciona del calendario */ },
            label = { Text("Fecha de la Rifa") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Button(onClick = { datePickerDialog.show() }) {
                    Text("Seleccionar")
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (nombre.isNotBlank() && fecha.isNotBlank()) {
                    db.insertarRifa(nombre, 0, fecha) // Inicializamos inscritos en 0
                    (contextCompose as? ComponentActivity)?.finish() // Volver a la actividad anterior
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}