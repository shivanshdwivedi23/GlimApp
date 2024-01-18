package com.infigeek.glimapp.networkService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Helper {

    private const val BASE_URL = "https://newsapi.org/v2/"

    fun getInstance(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}