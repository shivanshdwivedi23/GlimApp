package com.infigeek.glimapp.networkService

data class NewsJson(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)