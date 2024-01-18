// SearchAdapter.kt
package com.infigeek.glimapp.viewHolders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.infigeek.glimapp.NewsModel
import com.infigeek.glimapp.R

class SearchAdapter(
    private var newsList: List<NewsModel>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(newsModel: NewsModel)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item_layout, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsModel = newsList[position]

        holder.headlineTextView.text = newsModel.headLine
        holder.descriptionTextView.text = newsModel.description
        // Load image using Glide
        // Inside onBindViewHolder in SearchAdapter
        Glide.with(holder.itemView.context)
            .load(newsModel.image)
            .placeholder(R.drawable.defaultnews)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(RoundedCorners(16))
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(newsModel)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.newsImageView)
        val headlineTextView: TextView = itemView.findViewById(R.id.headlineTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }
    fun setData(newList: List<NewsModel>) {
        newsList = newList
        notifyDataSetChanged()
    }
}
