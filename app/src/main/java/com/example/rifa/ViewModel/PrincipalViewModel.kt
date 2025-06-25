package com.example.rifa.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifa.model.Auction
import com.example.rifa.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
class PrincipalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuctionRepository()

    private val _auctions = MutableStateFlow<List<Auction>>(emptyList())
    val auctions: StateFlow<List<Auction>> = _auctions

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    fun actualizarBusqueda(texto: String) {
        _searchText.value = texto
    }
    fun probarConexion() {
        viewModelScope.launch {
            try {
                val auctions = repository.getAuctions()
                Log.d("API Test", "Conectado. Total subastas: ${auctions.size}")
            } catch (e: Exception) {
                Log.e("API Test", "Error al conectarse: ${e.message}", e)
            }
        }
    }


    fun cargarSubastas() {
        viewModelScope.launch {
            try {
                val todas = repository.getAuctions()
                _auctions.value = todas.filter {
                    it.title.contains(_searchText.value, ignoreCase = true)
                }
            } catch (e: Exception) {
                _auctions.value = emptyList()
            }
        }
    }

    fun recargarSubastas() {
        cargarSubastas()
    }
}
