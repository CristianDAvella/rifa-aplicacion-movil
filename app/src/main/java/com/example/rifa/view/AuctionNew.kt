package com.example.rifa.view

import android.app.DatePickerDialog
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rifa.viewmodel.AuctionNewViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log

class AuctionNew : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuctionNewScreen()
        }
    }
}

@Composable
fun AuctionNewScreen(viewModel: AuctionNewViewModel = viewModel()) {
    val context = LocalContext.current
    val title by viewModel.title.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val selected = Calendar.getInstance()
            selected.set(year, month, day)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            viewModel.actualizarFecha(format.format(selected.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Subasta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { viewModel.actualizarTitulo(it) },
            label = { Text("Título de la Subasta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = endDate,
            onValueChange = { },
            label = { Text("Fecha de finalización") },
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
                viewModel.guardarAuction { success ->
                    if (success) {
                        (context as? ComponentActivity)?.finish()
                    } else {
                        Log.e("AuctionNew", "Falló la creación de la subasta")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

    }
}
