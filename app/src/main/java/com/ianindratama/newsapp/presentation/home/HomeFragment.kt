package com.ianindratama.newsapp.presentation.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.core.data.Resource
import com.ianindratama.newsapp.core.ui.adapter.NewsAdapter
import com.ianindratama.newsapp.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
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

        homeViewModel.news.observe(viewLifecycleOwner) { listOfNews ->
            if (listOfNews != null){
                when (listOfNews) {
                    is Resource.Loading -> binding.circularProgressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.circularProgressBar.visibility = View.GONE
                        newsAdapter.submitNewData(listOfNews.data!!)
                    }
                    is Resource.Error -> {
                        binding.circularProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error: ${listOfNews.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}