package com.example.rifa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifa.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.rifa.model.Auction
import java.util.UUID


class AuctionNewViewModel : ViewModel() {
    private val repository = AuctionRepository()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _endDate = MutableStateFlow("")
    val endDate: StateFlow<String> = _endDate

    fun actualizarTitulo(nuevoTitulo: String) {
        _title.value = nuevoTitulo
    }
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    fun actualizarDescripcion(nuevaDescripcion: String) {
        _description.value = nuevaDescripcion
    }


    fun actualizarFecha(nuevaFecha: String) {
        _endDate.value = nuevaFecha
    }

    fun guardarAuction(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val title = _title.value.trim()
            val description = _description.value.trim()
            val endDate = _endDate.value.trim()

            if (title.isBlank() || endDate.isBlank()) {
                Log.e("AuctionNewViewModel", "Título o fecha vacíos")
                onResult(false)
                return@launch
            }

            val auction = Auction(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                end_time = endDate,
                bids = emptyList()
            )


            try {
                val response = repository.postAuction(auction)
                if (response.isSuccessful) {
                    Log.d("AuctionNewViewModel", "Subasta creada correctamente")
                    onResult(true)
                } else {
                    val error = response.errorBody()?.string().orEmpty()
                    Log.e("AuctionNewViewModel", "Falló la creación: $error")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("AuctionNewViewModel", "Error al guardar subasta", e)
                onResult(false)
            }
        }
    }


}
