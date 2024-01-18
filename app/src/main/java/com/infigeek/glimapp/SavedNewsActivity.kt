package com.infigeek.glimapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infigeek.glimapp.architecture.NewsViewModel
import com.infigeek.glimapp.utility.appConstants.NEWS_CONTENT
import com.infigeek.glimapp.utility.appConstants.NEWS_DESCRIPTION
import com.infigeek.glimapp.utility.appConstants.NEWS_IMAGE_URL
import com.infigeek.glimapp.utility.appConstants.NEWS_PUBLICATION_TIME
import com.infigeek.glimapp.utility.appConstants.NEWS_SOURCE
import com.infigeek.glimapp.utility.appConstants.NEWS_TITLE
import com.infigeek.glimapp.utility.appConstants.NEWS_URL
import com.infigeek.glimapp.viewHolders.CustomAdapter

class SavedNewsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsData: MutableList<NewsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_news)

        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        newsData = mutableListOf()

        val adapter = CustomAdapter(newsData)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        // Get Saved News
        viewModel.getNewsFromDB(context = applicationContext)?.observe(this) {
            newsData.clear()
            newsData.addAll(it)
            adapter.notifyDataSetChanged()
        }

        adapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@SavedNewsActivity, NewsViewActivity::class.java).apply {
                    putExtra(NEWS_URL, newsData[position].url)
                    putExtra(NEWS_TITLE, newsData[position].headLine)
                    putExtra(NEWS_IMAGE_URL, newsData[position].image)
                    putExtra(NEWS_DESCRIPTION, newsData[position].description)
                    putExtra(NEWS_SOURCE, newsData[position].source)
                    putExtra(NEWS_PUBLICATION_TIME, newsData[position].time)
                    putExtra(NEWS_CONTENT, newsData[position].content)
                }
                startActivity(intent)
            }
        })
        adapter.setOnItemLongClickListener(object : CustomAdapter.OnItemLongClickListener {
            override fun onItemLongClick(position: Int) {
                // Delete saved news dialog
                recyclerView.findViewHolderForAdapterPosition(position)?.itemView?.setBackgroundColor(
                    getThemeColor(com.google.android.material.R.attr.colorPrimaryVariant)
                )

                val alertDialog = AlertDialog.Builder(this@SavedNewsActivity).apply {
                    setMessage("Delete this News?")
                    setTitle("Alert!")
                    setCancelable(false)

                    setPositiveButton("Yes") { _, _ ->
                        this@SavedNewsActivity.let {
                            viewModel.deleteNews(
                                it,
                                news = newsData[position]
                            )
                        }
                        adapter.notifyItemRemoved(position)
                        Toast.makeText(this@SavedNewsActivity, "Deleted!", Toast.LENGTH_SHORT).show()
                    }

                    setNegativeButton("No") { _, _ ->
                        recyclerView.findViewHolderForAdapterPosition(position)?.itemView?.setBackgroundColor(
                            getThemeColor(com.google.android.material.R.attr.colorPrimary)
                        )
                    }
                }.create()

                alertDialog.show()
            }
        })

        recyclerView.adapter = adapter
    }

    @ColorInt
    private fun Context.getThemeColor(@AttrRes attribute: Int) = TypedValue().let {
        theme.resolveAttribute(attribute, it, true)
        it.data
    }
}
