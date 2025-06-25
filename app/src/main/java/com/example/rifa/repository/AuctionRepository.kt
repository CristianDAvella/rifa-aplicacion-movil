package com.example.rifa.repository

import com.example.rifa.model.Auction
import com.example.rifa.model.CreateAuction
import com.example.rifa.network.RetrofitClient
import android.util.Log

class AuctionRepository {
    private val api = RetrofitClient.apiService

    suspend fun getAuctions(): List<Auction> {
        return api.getAuctions()
    }

    suspend fun getAuctionDetail(title: String): Auction {
        return api.getAuctionDetail(title)
    }

    suspend fun deleteAuction(title: String) {
        api.deleteAuction(title)
    }

    suspend fun createAuction(title: String, endTime: String): Boolean {
        return try {
            val auction = CreateAuction(title, endTime)
            Log.d("AuctionRepository", "Intentando crear subasta: $auction")
            api.createAuction(auction)
            true
        } catch (e: Exception) {
            Log.e("AuctionRepository", "Error al crear subasta", e)
            false
        }
    }

}
