package com.example.rifa.network

import com.example.rifa.model.Auction

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response
import com.example.rifa.model.BidRequest


interface AuctionApiService {
    @GET("auctions")
    suspend fun getAuctions(): List<Auction>

    @POST("auctions/")
    suspend fun postAuction(@Body auction: Auction): Response<Auction>

    @GET("auctions/{id}")
    suspend fun getAuctionDetail(@Path("id") id: String): Auction


    @POST("auctions/{auction_id}/bids")
    suspend fun postBid(@Path("auction_id") auctionId: String, @Body bid: BidRequest): Response<Unit>


    @DELETE("auctions/{id}")
    suspend fun deleteAuction(@Path("id") id: String): Response<Unit>

}
