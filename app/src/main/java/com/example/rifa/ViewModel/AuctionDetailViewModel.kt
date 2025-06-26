package com.example.rifa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifa.model.Bid
import com.example.rifa.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
class AuctionDetailViewModel : ViewModel() {
    private val repository = AuctionRepository()

    private val _bids = MutableStateFlow<List<Bid>>(emptyList())
    val bids: StateFlow<List<Bid>> = _bids

    private var auctionTitle: String = ""

    fun setAuctionTitle(title: String) {
        auctionTitle = title
        loadAuctionDetail()
    }

    private fun loadAuctionDetail() {
        viewModelScope.launch {
            try {
                val auction = repository.getAuctionDetail(auctionTitle)
                _bids.value = auction.bids
            } catch (e: Exception) {
                Log.e("AuctionDetailVM", "Error al cargar detalles", e)
            }
        }
    }


    fun toggleBidSelection(user: String) {
        _bids.value = _bids.value.map {
            if (it.user == user) it.copy(amount = if (it.amount > 0.0) 0.0 else 1.0) else it
        }
    }
    fun agregarPuja(asiento: String, monto: Double) {
        val nuevaPuja = Bid(
            user = asiento,
            amount = monto,
            timestamp = System.currentTimeMillis().toString()
        )

        Log.d("Puja", "Preparando para enviar puja: $nuevaPuja")  // 👈

        viewModelScope.launch {
            try {
                val response = repository.postBid(auctionTitle, nuevaPuja)
                Log.d("Puja", "POST ejecutado. Código: ${response.code()}")  // 👈

                if (response.isSuccessful) {
                    Log.d("Puja", "Puja enviada con éxito")
                    loadAuctionDetail() // actualiza lista de asientos ocupados
                } else {
                    Log.e("Puja", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Puja", "Excepción al enviar puja", e)
            }
        }
    }




    fun deleteAuction(onFinished: () -> Unit) {
        viewModelScope.launch {
            repository.deleteAuction(auctionTitle)
            onFinished()
        }
    }
}
