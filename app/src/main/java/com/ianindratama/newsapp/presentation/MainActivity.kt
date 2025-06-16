package com.ianindratama.newsapp.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.core.data.Resource
import com.ianindratama.newsapp.core.ui.adapter.NewsAdapter
import com.ianindratama.newsapp.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.setHasFixedSize(true)

        val newsAdapter = NewsAdapter()
        newsAdapter.setOnItemClickCallback(object: NewsAdapter.OnItemClickCallback{
            override fun onItemClicked(newsUrl: String?) {
                if (newsUrl != null){
                    val web = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(newsUrl)
                    )
                    startActivity(web)
                }
            }
        })

        binding.rvNews.adapter = newsAdapter

        mainViewModel.news.observe(this) { listOfNews ->
            if (listOfNews != null){
                when (listOfNews) {
                    is Resource.Loading -> binding.circularProgressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.circularProgressBar.visibility = View.GONE
                        newsAdapter.submitNewData(listOfNews.data!!)
                    }
                    is Resource.Error -> {
                        binding.circularProgressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Error: ${listOfNews.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}