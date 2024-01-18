package com.infigeek.glimapp

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val webView: WebView = findViewById(R.id.webView)

        val newsModel = intent.getSerializableExtra("news_model") as NewsModel?
        if (newsModel != null) {
            webView.loadUrl(newsModel.url ?: "")
        } else {
            // Handle error or show a default view
        }
    }
}
