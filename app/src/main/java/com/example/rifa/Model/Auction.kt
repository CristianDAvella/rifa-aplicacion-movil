package com.example.rifa.model

data class Auction(
    val id: String,
    val title: String,
    val description: String,
    val end_time: String,
    val bids: List<Bid> = emptyList()
)


data class Bid(
    val user: String,
    val amount: Double,
    val timestamp: String
)
data class BidRequest(
    val user: String,
    val amount: Double,
    val timestamp: String
)

data class AuctionWithMaxBid(
    val id: String,
    val title: String,
    val end_time: String,
    val bidsCount: Int,
    val maxBid: Double
)
