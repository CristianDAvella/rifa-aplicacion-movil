package com.example.rifa.repository

import com.example.rifa.model.Auction
import retrofit2.Response
import com.example.rifa.network.RetrofitInstance
import com.example.rifa.model.Bid

class AuctionRepository {
    private val api = RetrofitInstance.api

    suspend fun getAuctions(): List<Auction> {
        return api.getAuctions()
    }

    suspend fun getAuctionDetail(title: String): Auction {
        return api.getAuctionDetail(title)
    }

    suspend fun deleteAuction(title: String) {
        api.deleteAuction(title)
    }

    suspend fun postBid(title: String, bid: Bid): Response<Unit> {
        return api.postBid(title, bid)
    }


    suspend fun postAuction(auction: Auction): Response<Auction> {
        return api.postAuction(auction)
    }
}
