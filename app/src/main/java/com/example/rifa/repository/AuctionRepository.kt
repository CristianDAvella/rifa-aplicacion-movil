package com.example.rifa.repository

import com.example.rifa.model.Auction
import retrofit2.Response
import com.example.rifa.network.RetrofitInstance
import com.example.rifa.model.BidRequest

class AuctionRepository {
    private val api = RetrofitInstance.api

    suspend fun getAuctions(): List<Auction> {
        return api.getAuctions()
    }

    suspend fun getAuctionDetail(id: String): Auction {
        return api.getAuctionDetail(id)
    }

    suspend fun deleteAuction(id: String): Response<Unit> {
        return api.deleteAuction(id)
    }


    suspend fun postBid(auctionId: String, bid: BidRequest): Response<Unit> {
        return api.postBid(auctionId, bid)
    }


    suspend fun postAuction(auction: Auction): Response<Auction> {
        return api.postAuction(auction)
    }
}
