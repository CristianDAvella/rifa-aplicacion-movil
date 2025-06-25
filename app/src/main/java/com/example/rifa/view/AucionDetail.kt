package com.example.rifa.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rifa.viewmodel.AuctionDetailViewModel

class AuctionDetail : ComponentActivity() {
    private val viewModel: AuctionDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("nombre") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: ""

        viewModel.setAuctionTitle(title)

        setContent {
            AuctionDetailScreen(
                title = title,
                fecha = fecha,
                viewModel = viewModel,
                onClose = { finish() }
            )
        }
    }
}

@Composable
fun AuctionDetailScreen(
    title: String,
    fecha: String,
    viewModel: AuctionDetailViewModel,
    onClose: () -> Unit
) {
    val bids by viewModel.bids.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Subasta: $title", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Fecha: $fecha")
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(bids) { bid ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { viewModel.toggleBidSelection(bid.user) }
                        .background(if (bid.amount > 0) Color.LightGray else Color.White)
                        .padding(8.dp)
                ) {
                    Text("Usuario: ${bid.user}", modifier = Modifier.weight(1f))
                    Text("Oferta: $${bid.amount}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onClose() }) {
                Text("Cerrar")
            }

            Button(
                onClick = { viewModel.deleteAuction { onClose() } },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminar")
            }
        }
    }
}
