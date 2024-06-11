package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.datasources.model.ArticlesItemDataClass
import com.dicoding.asclepius.databinding.NewsCardBinding

class NewsAdapter: ListAdapter<ArticlesItemDataClass, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = NewsCardBinding.inflate(inflater, parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bindItem(currentArticle)
    }

    class MyViewHolder(private val binding: NewsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(articles: ArticlesItemDataClass) {
            binding.newsTitle.text = articles.title
            binding.newsDescription.text = articles.description
            if (articles.urlToImage != null) {
                Glide.with(binding.newsImage.context)
                    .load(articles.urlToImage)
                    .into(binding.newsImage)
            }

            binding.root.setOnClickListener {
                val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse(articles.url))
                binding.root.context.startActivity(intentUrl)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItemDataClass>() {
            override fun areItemsTheSame(oldItem: ArticlesItemDataClass, newItem: ArticlesItemDataClass): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ArticlesItemDataClass, newItem: ArticlesItemDataClass): Boolean {
                return oldItem == newItem
            }
        }
    }
}