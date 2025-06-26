package com.example.rifa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifa.model.Bid
import com.example.rifa.model.BidRequest
import com.example.rifa.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AuctionDetailViewModel : ViewModel() {
    private val repository = AuctionRepository()

    private val _bids = MutableStateFlow<List<Bid>>(emptyList())
    val bids: StateFlow<List<Bid>> = _bids

    private lateinit var auctionId: String
    private lateinit var auctionTitle: String

    fun setAuction(auctionId: String, title: String) {
        this.auctionId = auctionId
        this.auctionTitle = title
        loadAuctionDetail()
    }

    private fun loadAuctionDetail() {
        viewModelScope.launch {
            try {
                val auction = repository.getAuctionDetail(auctionId)
                _bids.value = auction.bids
                Log.d("AuctionDetailVM", "Pujas recibidas: ${auction.bids}")
            } catch (e: Exception) {
                Log.e("AuctionDetailVM", "Error al cargar detalles", e)
            }
        }
    }



    fun agregarPuja(asiento: String, monto: Double) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val isoTimestamp = sdf.format(Date())

        val nuevaPuja = BidRequest(
            user = asiento,
            amount = monto,
            timestamp = isoTimestamp
        )

        Log.d("Puja", "Preparando para enviar puja: $nuevaPuja")

        viewModelScope.launch {
            try {
                Log.d("Puja", "Usando auctionId=$auctionId, title=$auctionTitle para enviar puja")

                val response = repository.postBid(auctionId, nuevaPuja)
                Log.d("Puja", "POST ejecutado. Código: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("Puja", "Puja enviada con éxito")
                    loadAuctionDetail()
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
            try {
                val response = repository.deleteAuction(auctionId)
                if (response.isSuccessful) {
                    onFinished()
                } else {
                    Log.e("Delete", "Falló el borrado: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Delete", "Error eliminando subasta", e)
            }
        }
    }


}
