package com.example.rifa.network

import com.example.rifa.model.Auction

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response
import com.example.rifa.model.Bid


interface AuctionApiService {
    @GET("auctions")
    suspend fun getAuctions(): List<Auction>

    @POST("auctions/")
    suspend fun postAuction(@Body auction: Auction): Response<Auction>

    @GET("auctions/{title}")
    suspend fun getAuctionDetail(@Path("title") title: String): Auction

    @POST("auctions/{title}/bids")
    suspend fun postBid(@Path("title") title: String, @Body bid: Bid): Response<Unit>

    @DELETE("auctions/{title}")
    suspend fun deleteAuction(@Path("title") title: String)
}
