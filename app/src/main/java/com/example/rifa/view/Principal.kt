package com.example.rifa.view

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rifa.viewmodel.PrincipalViewModel
import android.app.Activity

@Composable
fun Principal() {
    val context = LocalContext.current
    val viewModel: PrincipalViewModel = viewModel()
    val auctions by viewModel.auctions.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.recargarSubastas()
        }
    }


    LaunchedEffect(Unit) {
        viewModel.cargarSubastas()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = searchText,
                onValueChange = viewModel::actualizarBusqueda,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Buscar subasta") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = viewModel::cargarSubastas) {
                Text("Buscar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(auctions) { auction ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            val intent = Intent(context, AuctionDetail::class.java).apply {
                                putExtra("title", auction.title)
                                putExtra("end_time", auction.end_time)
                                putExtra("bids_count", auction.bids.size)
                            }
                            launcher.launch(intent)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Título: ${auction.title}", style = MaterialTheme.typography.titleMedium)
                        Text("Ofertas: ${auction.bids.size}")
                        Text("Finaliza: ${auction.end_time}")
                    }
                }
            }
        }
        if (auctions.isEmpty()) {
            Text("No hay subastas disponibles.", modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, AuctionNew::class.java)
                launcher.launch(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Subasta nueva")
        }
    }
}
