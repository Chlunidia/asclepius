package com.dicoding.asclepius.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.datasources.model.ArticlesItemDataClass
import com.dicoding.asclepius.state.FetchStatus
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.viewmodel.ResultViewModel

class ResultActivity : BaseActivity() {
    private lateinit var binding: ActivityResultBinding

    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customizeActionBar()

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val analysisResult = intent.getStringExtra(EXTRA_RESULT)

        imageUri?.let {
            Log.d("Image URI", "displayImage: $it")
            binding.resultImage.setImageURI(it)
        }

        analysisResult?.let {
            Log.d("Result", "displayResult: $it")
            binding.resultText.text = it
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvNews.layoutManager = layoutManager

        viewModel.news.observe(this) { news ->
            when (news) {
                is FetchStatus.Loading -> {
                    binding.shimmerLayout.startShimmer()
                }
                is FetchStatus.Success -> {
                    displayNewsRecyclerView()
                    displayNewsData(news.data)
                }
                is FetchStatus.Error -> {
                    Log.e("ResultActivity", "Error: $news")
                    Toast.makeText(this, "Error: $news", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayNewsData(newsArticles: List<ArticlesItemDataClass>) {
        val newsAdapter = NewsAdapter()
        newsAdapter.submitList(newsArticles)
        binding.rvNews.adapter = newsAdapter
    }

    private fun displayNewsRecyclerView() {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvNews.visibility = View.VISIBLE
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}