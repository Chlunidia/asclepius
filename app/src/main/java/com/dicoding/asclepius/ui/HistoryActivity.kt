package com.dicoding.asclepius.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.datasources.local.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import com.dicoding.asclepius.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setCustomView(R.layout.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple)))

        val layoutManager = LinearLayoutManager(this)
        binding.historyList.layoutManager = layoutManager

        viewModel.historyList.observe(this){
            displayHistoryData(it)
        }
    }

    private fun displayHistoryData(consumer:List<HistoryEntity>){
        val adapter = HistoryAdapter()
        adapter.submitList(consumer)
        binding.historyList.adapter = adapter
    }
}