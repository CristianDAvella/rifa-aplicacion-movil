package com.example.rifa.network

import com.example.rifa.model.Auction
import com.example.rifa.model.CreateAuction
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuctionApiService {
    @GET("auctions")
    suspend fun getAuctions(): List<Auction>

    @POST("auctions")
    suspend fun createAuction(@Body auction: CreateAuction)

    @GET("auctions/{title}")
    suspend fun getAuctionDetail(@Path("title") title: String): Auction

    @DELETE("auctions/{title}")
    suspend fun deleteAuction(@Path("title") title: String)
}
