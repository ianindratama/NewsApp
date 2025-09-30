package com.ianindratama.newsapp.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ianindratama.newsapp.core.databinding.NewsItemLayoutBinding
import com.ianindratama.newsapp.core.presentation.model.news.NewsUiModel
import com.ianindratama.newsapp.core.utils.parseNewsTimestamp

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    private val asyncListDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitNewData(newListOfNews: List<NewsUiModel>) {
        asyncListDiffer.submitList(newListOfNews)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            NewsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val newsData = asyncListDiffer.currentList[position]
        holder.bind(newsData)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(newsData)
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    class ListViewHolder(private val binding: NewsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsData: NewsUiModel) {
            Glide.with(binding.root.context)
                .load(newsData.urlToImage)
                .into(binding.imgItemPhoto)

            binding.tvItemTitle.text = newsData.title
            binding.tvItemDescription.text = newsData.description
            binding.tvItemPublishedAt.text = parseNewsTimestamp(newsData.publishedAt)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(newsData: NewsUiModel)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<NewsUiModel> =
            object : DiffUtil.ItemCallback<NewsUiModel>() {
                override fun areItemsTheSame(oldItem: NewsUiModel, newItem: NewsUiModel): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: NewsUiModel, newItem: NewsUiModel): Boolean {
                    return oldItem == newItem
                }
            }
    }
}