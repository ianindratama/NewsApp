package com.ianindratama.newsapp.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.presentation.utils.parseNewsTimestamp

// TODO: Buat model News untuk layer presentation
class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    private val asyncListDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitNewData(newListOfNews: List<News>) {
        asyncListDiffer.submitList(newListOfNews)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val newsData = asyncListDiffer.currentList[position]

        Glide.with(holder.itemView.context)
            .load(newsData.urlToImage)
            .into(holder.imgPhoto)

        holder.tvTitle.text = newsData.title
        holder.tvDescription.text = newsData.description
        holder.tvPublishedAt.text = parseNewsTimestamp(newsData.publishedAt)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(newsData)
        }

    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_item_title)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
        var tvPublishedAt: TextView = itemView.findViewById(R.id.tv_item_publishedAt)
    }

    interface OnItemClickCallback {
        fun onItemClicked(newsData: News)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<News> =
            object : DiffUtil.ItemCallback<News>() {
                override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                    return oldItem == newItem
                }
            }
    }
}