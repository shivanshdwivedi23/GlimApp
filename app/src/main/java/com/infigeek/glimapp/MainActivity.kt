package com.infigeek.glimapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.infigeek.glimapp.utility.appConstants.ENTERTAINMENT
import com.infigeek.glimapp.utility.appConstants.HEALTH
import com.infigeek.glimapp.utility.appConstants.SPORTS
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.infigeek.glimapp.architecture.NewsViewModel
import com.infigeek.glimapp.utility.appConstants.BUSINESS
import com.infigeek.glimapp.utility.appConstants.GENERAL
import com.infigeek.glimapp.utility.appConstants.HOME
import com.infigeek.glimapp.utility.appConstants.SCIENCE
import com.infigeek.glimapp.utility.appConstants.TECHNOLOGY
import com.infigeek.glimapp.utility.appConstants.TOTAL_NEWS_TAB
import com.infigeek.glimapp.viewHolders.FragmentAdapter

class MainActivity : AppCompatActivity() {
    // Tabs Title
    private val newsCategories = arrayOf(
        HOME,TECHNOLOGY,HEALTH,SCIENCE,SPORTS,
        ENTERTAINMENT,BUSINESS
    )
    private lateinit var viewModels: NewsViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewScroller: ViewPager2
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private var requestCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Action Bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        tabLayout = findViewById(R.id.tab_layout)
        viewScroller = findViewById(R.id.view_pager)
        shimmerLayout = findViewById(R.id.shimmer_layout)
        viewModels = ViewModelProvider(this)[NewsViewModel::class.java]

        if (!isNetworkAvailable(applicationContext)) {
            shimmerLayout.visibility = View.GONE
            val showError: TextView = findViewById(R.id.display_errors)
            showError.text = getString(R.string.internet_warming)
            showError.visibility = View.VISIBLE
        }

        // Send request call for news data
        requestNews(GENERAL, generalNews)
        requestNews(TECHNOLOGY, techNews)
        requestNews(HEALTH, healthNews)
        requestNews(SCIENCE, scienceNews)
        requestNews(SPORTS, sportsNews)
        requestNews(ENTERTAINMENT, entertainmentNews)
        requestNews(BUSINESS, businessNews)

        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        viewScroller.adapter = fragmentAdapter
        viewScroller.visibility = View.GONE
        val fabSavedNews: FloatingActionButton = findViewById(R.id.fabSavedNews)
        fabSavedNews.setOnClickListener {
            val intent = Intent(this@MainActivity, SavedNewsActivity::class.java)
            startActivity(intent)
        }
    }
    private fun requestNews(newsCategory: String, newsData: MutableList<NewsModel>) {
        viewModels.getNews(category = newsCategory)?.observe(this) {
            newsData.addAll(it)
            requestCount += 1

            // If main fragment loaded then attach the fragment to viewPager
            if (newsCategory == GENERAL) {
                shimmerLayout.stopShimmer()
                shimmerLayout.hideShimmer()
                shimmerLayout.visibility = View.GONE
                setViewPager()
            }
            if (requestCount == TOTAL_NEWS_TAB) {
                viewScroller.offscreenPageLimit = 7
            }
        }
    }
    private fun setViewPager() {
        if (!apiRequestError) {
            viewScroller.visibility = View.VISIBLE
            TabLayoutMediator(tabLayout, viewScroller) { tab, position ->
                tab.text = newsCategories[position]
            }.attach()
        } else {
            val showError: TextView = findViewById(R.id.display_errors)
            showError.text = errorMessage
            showError.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item_mainactivity, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.searchnews_menu -> {
                startActivity(Intent(this, SearchNewsActivity::class.java))
                return true
            }
            R.id.fabSavedNews -> {
                val intent = Intent(this, SavedNewsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // Check internet connection
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
    companion object {
        var generalNews: ArrayList<NewsModel> = ArrayList()
        var techNews: MutableList<NewsModel> = mutableListOf()
        var healthNews: MutableList<NewsModel> = mutableListOf()
        var scienceNews: MutableList<NewsModel> = mutableListOf()
        var sportsNews: MutableList<NewsModel> = mutableListOf()
        var entertainmentNews: MutableList<NewsModel> = mutableListOf()
        var businessNews: MutableList<NewsModel> = mutableListOf()
        var apiRequestError = false
        var errorMessage = "error"
    }
}