package com.example.rifa.model
data class Auction(
    val title: String,
    val description: String?,
    val end_time: String,
    val bids: List<Bid> = emptyList()
)

data class Bid(
    val user: String,
    val amount: Double,
    val timestamp: String
)

data class CreateAuction(
    val title: String,
    val end_time: String
)