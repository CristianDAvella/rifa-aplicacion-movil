package com.example.rifa.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: AuctionApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://web-production-94efc.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuctionApiService::class.java)
    }
}
