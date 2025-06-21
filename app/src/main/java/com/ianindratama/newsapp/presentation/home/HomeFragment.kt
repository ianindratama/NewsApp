package com.ianindratama.newsapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.core.data.Resource
import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.core.ui.adapter.NewsAdapter
import com.ianindratama.newsapp.databinding.FragmentHomeBinding
import com.ianindratama.newsapp.presentation.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
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

        (requireActivity() as MainActivity).updateAppBarTitle(getString(R.string.app_name))

        binding.appBarWithSearch.setContent {
            val searchQuery by homeViewModel.searchNewsQuery.collectAsState()

            // TODO: Optional - Add Theme implementation for Compose
            SearchAppBar(
                modifier = Modifier,
                query = searchQuery,
                handleQuery = { newValue ->
                    homeViewModel.updateSearchQuery(newValue)
                },
                clearQuery = {
                    homeViewModel.clearSearchQuery()
                }
            )
        }

        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.setHasFixedSize(true)

        val newsAdapter = NewsAdapter()
        newsAdapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(newsData: News) {
                val toDetailActivity =
                    HomeFragmentDirections.actionHomeFragmentToDetailActivity(newsData)

                view.findNavController().navigate(toDetailActivity)
            }
        })

        binding.rvNews.adapter = newsAdapter

        homeViewModel.listOfNews.observe(viewLifecycleOwner) { listOfNews ->
            if (listOfNews != null) {
                when (listOfNews) {
                    is Resource.Loading -> binding.circularProgressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.circularProgressBar.visibility = View.GONE
                        newsAdapter.submitNewData(listOfNews.data!!)
                    }

                    is Resource.Error -> {
                        binding.circularProgressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Error: ${listOfNews.message}",
                            Toast.LENGTH_SHORT
                        ).show()
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

@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    query: String?,
    handleQuery: (query: String) -> Unit,
    clearQuery: () -> Unit
) {
    var inputHasFocus by remember {
        mutableStateOf(false)
    }

    val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged {
                    inputHasFocus = it.hasFocus
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                unfocusedLabelColor = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
            ),
            singleLine = true,
            value = query ?: "",
            onValueChange = {
                handleQuery(it)
            },
            trailingIcon = {
                if (query.isNullOrEmpty() && !inputHasFocus) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.cd_search_news)
                    )
                } else {
                    IconButton(onClick = {
                        localFocusManager.clearFocus(true)
                        localSoftwareKeyboardController?.hide()
                        clearQuery()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.cd_clear_search)
                        )
                    }
                }
            }
        )

        if (!inputHasFocus && query.isNullOrEmpty()) {
            val onSurfaceWithAlpha = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)

            Text(
                modifier = Modifier.padding(start = 16.dp),
                color = onSurfaceWithAlpha,
                text = stringResource(R.string.hint_search_news),
            )
        }
    }
}