package com.example.rifa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifa.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class AuctionNewViewModel : ViewModel() {
    private val repository = AuctionRepository()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _endDate = MutableStateFlow("")
    val endDate: StateFlow<String> = _endDate

    fun actualizarTitulo(nuevoTitulo: String) {
        _title.value = nuevoTitulo
    }

    fun actualizarFecha(nuevaFecha: String) {
        _endDate.value = nuevaFecha
    }


    fun guardarAuction(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.createAuction(_title.value, _endDate.value)
            onResult(success)
        }
    }
}
