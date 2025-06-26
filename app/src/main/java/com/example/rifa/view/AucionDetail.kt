package com.example.rifa.view

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.lazy.grid.*

class AuctionDetail : ComponentActivity() {
    private val viewModel: AuctionDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("title") ?: ""
        val fecha = intent.getStringExtra("end_time") ?: ""

        if (title.isBlank()) {
            finish()
            return
        }

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

    // Estado local para la UI
    var selectedSeat by remember { mutableStateOf<String?>(null) }
    var bidAmount by remember { mutableStateOf("") }

    val seatNumbers = (0..99).map { it.toString().padStart(2, '0') }
    val occupiedSeats = bids.map { it.user }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Subasta: $title", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Fecha de cierre: $fecha")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Asientos disponibles", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(seatNumbers.size) { index ->
                val seat = seatNumbers[index]
                val isOccupied = seat in occupiedSeats

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(32.dp)
                        .background(
                            when {
                                isOccupied -> Color.Red
                                selectedSeat == seat -> Color.Cyan
                                else -> Color.LightGray
                            }
                        )
                        .border(1.dp, Color.Black)
                        .clickable(enabled = !isOccupied) {
                            selectedSeat = seat
                            Log.d("Asiento", "Asiento $seat seleccionado")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        seat,
                        fontSize = 16.sp,
                        color = if (isOccupied) Color.White else Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedSeat?.let { seat ->
            Text("Asiento seleccionado: $seat", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = bidAmount,
                onValueChange = { bidAmount = it },
                label = { Text("Ingrese su puja") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val monto = bidAmount.toDoubleOrNull()
                    if (monto != null) {
                        viewModel.agregarPuja(seat, monto)
                        bidAmount = ""
                        selectedSeat = null
                    } else {
                        Log.e("Puja", "Monto inválido")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Puja")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onClose) {
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