package com.example.rifa.viewmodel

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.rifa.model.Auction
import com.example.rifa.model.AuctionWithMaxBid
import com.example.rifa.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrincipalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuctionRepository()

    private val _auctions = MutableStateFlow<List<AuctionWithMaxBid>>(emptyList())
    val auctions: StateFlow<List<AuctionWithMaxBid>> = _auctions

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    fun actualizarBusqueda(text: String) {
        _searchText.value = text
    }

    fun cargarSubastas() {
        viewModelScope.launch {
            try {
                val result = repository.getAuctions()
                val filtradas = if (_searchText.value.isNotBlank()) {
                    result.filter { it.title.contains(_searchText.value, ignoreCase = true) }
                } else {
                    result
                }

                _auctions.value = filtradas.map { auction ->
                    AuctionWithMaxBid(
                        id = auction.id,
                        title = auction.title,
                        end_time = auction.end_time,
                        bidsCount = auction.bids.size,
                        maxBid = auction.bids.maxByOrNull { it.amount }?.amount ?: 0.0
                    )
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
