// SearchNewsActivity.kt
package com.infigeek.glimapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infigeek.glimapp.networkService.Helper
import com.infigeek.glimapp.networkService.NewsApi
import com.infigeek.glimapp.networkService.NewsJson
import com.infigeek.glimapp.viewHolders.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchNewsActivity : AppCompatActivity(), SearchAdapter.OnItemClickListener {

    private lateinit var apiInterface: NewsApi
    private lateinit var searchAdapter: SearchAdapter
    private var newsData: List<NewsModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_news)

        apiInterface = Helper.getInstance().create(NewsApi::class.java)
        searchAdapter = SearchAdapter(newsData, this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = searchAdapter

        val searchView: SearchView = findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchNews(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if needed
                return false
            }
        })

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val selectedNews = newsData[position]
                        openNewsDetail(selectedNews)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        // Handle long item click if needed
                    }
                })
        )
    }
    private fun searchNews(query: String) {
        Log.d("SearchNewsActivity", "Query submitted: $query")
        if (query.isBlank()) {
            Toast.makeText(this@SearchNewsActivity, "Please enter a search query", Toast.LENGTH_SHORT).show()
            return
        }
        val apiKey = "9e08c75e6f174bcf8317bd682bc630fa"
        val call = apiInterface.searchNews(query, apiKey)
        call.enqueue(object : Callback<NewsJson> {
            override fun onResponse(call: Call<NewsJson>, response: Response<NewsJson>) {
                if (response.isSuccessful) {
                    response.body()?.let { newsDataFromJson ->
                        // Update the list with new search results
                        newsData = newsDataFromJson.articles.map { article ->
                            NewsModel(
                                headLine = article.title ?: "",
                                image = article.urlToImage ?: "",
                                description = article.description ?: "",
                                url = article.url ?: "",
                                source = article.source?.name ?: "Unknown Source",
                                time = article.publishedAt ?: "",
                                content = article.content ?: "No content available"
                            )
                        }

                        // Update the adapter
                        searchAdapter.setData(newsData)
                    }
                } else {
                    Log.e("APIResponse", "Unsuccessful response: ${response.code()}")
                    // Handle error cases if needed
                    Toast.makeText(
                        this@SearchNewsActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<NewsJson>, t: Throwable) {
                Log.e("APIResponse", "Failure: ${t.message}")
                Toast.makeText(this@SearchNewsActivity, "Network error", Toast.LENGTH_SHORT).show()

                // Log the error in more detail
                t.printStackTrace()
            }
        })
    }

    private fun openNewsDetail(newsModel: NewsModel) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra("news_model", newsModel)
        startActivity(intent)
    }

    // Implement the onItemClick method
    override fun onItemClick(newsModel: NewsModel) {
        openNewsDetail(newsModel)
    }
}
