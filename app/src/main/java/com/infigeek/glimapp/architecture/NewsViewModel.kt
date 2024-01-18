package com.infigeek.glimapp.architecture

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infigeek.glimapp.NewsModel

class NewsViewModel : ViewModel() {
    private var newsLiveData: MutableLiveData<List<NewsModel>>? = null
    //get news from API
    fun getNews(category: String?): MutableLiveData<List<NewsModel>>? {
        newsLiveData = category.let { NewsRepo().getNewsApiCall(it) }
        return newsLiveData
    }
    var newsData: LiveData<List<NewsModel>>? = null
    fun insertNews(context: Context, news: NewsModel) {
        NewsRepo.insertNews(context, news)
    }
    fun deleteNews(context: Context, news: NewsModel) {
        NewsRepo.deleteNews(context, news)
    }
    fun getNewsFromDB(context: Context): LiveData<List<NewsModel>>? {
        newsData = NewsRepo.getAllNews(context)
        return newsData
    }
}
