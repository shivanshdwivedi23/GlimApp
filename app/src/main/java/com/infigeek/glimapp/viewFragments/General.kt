package com.infigeek.glimapp.viewFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infigeek.glimapp.utility.appConstants.INITIAL_POSITION
import com.infigeek.glimapp.MainActivity
import com.infigeek.glimapp.NewsModel
import com.infigeek.glimapp.NewsViewActivity
import com.infigeek.glimapp.R
import com.infigeek.glimapp.SearchNewsActivity
import com.infigeek.glimapp.utility.appConstants.NEWS_CONTENT
import com.infigeek.glimapp.utility.appConstants.NEWS_DESCRIPTION
import com.infigeek.glimapp.utility.appConstants.NEWS_IMAGE_URL
import com.infigeek.glimapp.utility.appConstants.NEWS_PUBLICATION_TIME
import com.infigeek.glimapp.utility.appConstants.NEWS_SOURCE
import com.infigeek.glimapp.utility.appConstants.NEWS_TITLE
import com.infigeek.glimapp.utility.appConstants.NEWS_URL
import com.infigeek.glimapp.utility.appConstants.TOP_HEADLINES_COUNT
import com.infigeek.glimapp.viewHolders.CustomAdapter
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.squareup.picasso.Picasso

class General : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carouselView: CarouselView
    private lateinit var adapter: CustomAdapter
    private lateinit var newsDataForTopHeadlines: List<NewsModel>
    private lateinit var newsDataForDown: List<NewsModel>
    var position = INITIAL_POSITION
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_general, container, false)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

        // Setting recyclerViews adapter
        newsDataForTopHeadlines = MainActivity.generalNews.take(TOP_HEADLINES_COUNT)
        newsDataForDown = MainActivity.generalNews.drop(TOP_HEADLINES_COUNT)
        adapter = CustomAdapter(newsDataForDown)
        recyclerView.adapter = adapter

        carouselView = view.findViewById<CarouselView>(R.id.home_carousel)
        if (!isInSearchActivity()) {
            carouselView.apply {
                size = newsDataForTopHeadlines.size
                autoPlay = true
                indicatorAnimationType = IndicatorAnimationType.THIN_WORM
                carouselOffset = OffsetType.START  // Change to OffsetType.START
                setCarouselViewListener { view, carouselPosition -> // Change the parameter name to avoid conflicts
                    if (carouselPosition >= 0 && carouselPosition < newsDataForTopHeadlines.size) {
                        val imageView = view.findViewById<ImageView>(R.id.img)
                        Picasso.get()
                            .load(newsDataForTopHeadlines[carouselPosition].image)
                            .fit()
                            .centerCrop()
                            .error(R.drawable.samplenews)
                            .into(imageView)
                        val newsTitle = view.findViewById<TextView>(R.id.headline)
                        newsTitle.text = newsDataForTopHeadlines[carouselPosition].headLine
                        view.setOnClickListener {
                            if (carouselPosition < newsDataForTopHeadlines.size) {
                                val intent = Intent(context, NewsViewActivity::class.java).apply {
                                    putExtra(
                                        NEWS_URL,
                                        newsDataForTopHeadlines[carouselPosition].url
                                    )
                                    putExtra(
                                        NEWS_TITLE,
                                        newsDataForTopHeadlines[carouselPosition].headLine
                                    )
                                    putExtra(
                                        NEWS_IMAGE_URL,
                                        newsDataForTopHeadlines[carouselPosition].image
                                    )
                                    putExtra(
                                        NEWS_DESCRIPTION,
                                        newsDataForTopHeadlines[carouselPosition].description
                                    )
                                    putExtra(
                                        NEWS_SOURCE,
                                        newsDataForTopHeadlines[carouselPosition].source
                                    )
                                    putExtra(
                                        NEWS_PUBLICATION_TIME,
                                        newsDataForTopHeadlines[carouselPosition].time
                                    )
                                    putExtra(
                                        NEWS_CONTENT,
                                        newsDataForTopHeadlines[carouselPosition].content
                                    )
                                }
                                startActivity(intent)
                            }
                        }
                    }
                }
                // show the CarouselView
                show()
            }
        }
        else{
            carouselView.visibility = View.GONE
        }
        // listitem onClick
        adapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, NewsViewActivity::class.java).apply {
                    putExtra(NEWS_URL, newsDataForDown[position].url)
                    putExtra(NEWS_TITLE, newsDataForDown[position].headLine)
                    putExtra(NEWS_IMAGE_URL, newsDataForDown[position].image)
                    putExtra(NEWS_DESCRIPTION, newsDataForDown[position].description)
                    putExtra(NEWS_SOURCE, newsDataForDown[position].source)
                    putExtra(NEWS_PUBLICATION_TIME, newsDataForDown[position].time)
                    putExtra(NEWS_CONTENT, newsDataForDown[position].content)
                }

                startActivity(intent)
            }
        })

        // Ignore
        adapter.setOnItemLongClickListener(object : CustomAdapter.OnItemLongClickListener {
            override fun onItemLongClick(position: Int) = Unit
        })

        return view
    }
    private fun isInSearchActivity(): Boolean {
        return activity is SearchNewsActivity
    }
}