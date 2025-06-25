package com.ianindratama.newsapp.presentation.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.databinding.ActivityDetailBinding
import com.ianindratama.newsapp.presentation.utils.cleanNewsContent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModel {
        val newsId = DetailActivityArgs.fromBundle(intent.extras as Bundle).newsDataId
        parametersOf(newsId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.activityDetail) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.appBarTitle.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        detailViewModel.favoriteNews.observe(this) { newsData ->
            newsData.let {
                Glide.with(this@DetailActivity)
                    .load(it.urlToImage)
                    .into(binding.ivNewsImage)

                binding.tvNewsTitle.text = it.title
                binding.tvNewsSource.text = getString(R.string.news_source, it.source)

                binding.tvNewsContent.text = it.content
                binding.tvNewsContent.cleanNewsContent(it.content, it.description, it.url)

                val author = it.author
                binding.tvNewsAuthor.text = getString(
                    R.string.news_author,
                    author.ifEmpty { it.source }
                )

                setFavoriteNewsImage(it.isFavorite)
            }
        }

        binding.fabFavorite.setOnClickListener {
            detailViewModel.updateFavoriteNewsStatus()
        }
    }

    private fun setFavoriteNewsImage(isFavorite: Boolean) {
        val isFavoriteImage =
            if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24

        binding.fabFavorite.setImageResource(isFavoriteImage)
    }
}