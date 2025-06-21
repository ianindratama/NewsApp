package com.ianindratama.newsapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.core.ui.adapter.NewsAdapter
import com.ianindratama.newsapp.databinding.FragmentFavoritesBinding
import com.ianindratama.newsapp.presentation.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).updateAppBarTitle(getString(R.string.favorites_fragment_title))

        binding.rvFavoritesNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoritesNews.setHasFixedSize(true)

        val newsAdapter = NewsAdapter()
        newsAdapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(newsData: News) {
                val toDetailActivity =
                    FavoritesFragmentDirections.actionFavoritesFragmentToDetailActivity(newsData)

                view.findNavController().navigate(toDetailActivity)
            }
        })

        binding.rvFavoritesNews.adapter = newsAdapter

        favoritesViewModel.favoritesNews.observe(viewLifecycleOwner) { listOfFavoriteNews ->
            newsAdapter.submitNewData(listOfFavoriteNews)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}