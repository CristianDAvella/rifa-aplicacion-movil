package com.example.rifa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifa.model.Bid
import com.example.rifa.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
            val auction = repository.getAuctionDetail(auctionTitle)
            _bids.value = auction.bids
        }
    }

    fun toggleBidSelection(user: String) {
        _bids.value = _bids.value.map {
            if (it.user == user) it.copy(amount = if (it.amount > 0.0) 0.0 else 1.0) else it
        }
    }

    fun deleteAuction(onFinished: () -> Unit) {
        viewModelScope.launch {
            repository.deleteAuction(auctionTitle)
            onFinished()
        }
    }
}
