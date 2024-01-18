package com.infigeek.glimapp.networkService

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("/v2/top-headlines")
    fun getNews(@Query("country") country : String, @Query("category") category : String?, @Query("apiKey") key : String) : Call<NewsJson>
    @GET("/v2/everything")
    fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsJson>
}