package com.securetech.wallpapers.ui.categories

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.securetech.wallpapers.databinding.FragmentCategoriesBinding
import com.securetech.wallpapers.ui.UiState
import com.securetech.wallpapers.ui.adapter.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoriesViewModel by viewModels()

    private val categoriesAdapter = CategoriesAdapter { category ->
        val isSearchCategory = category.id.startsWith(CategoriesViewModel.SEARCH_CATEGORY_PREFIX)
        val searchQuery = if (isSearchCategory) category.name else ""
        val action = CategoriesFragmentDirections
            .actionCategoriesToWallpaperList(
                categoryId = if (isSearchCategory) "" else category.id,
                categoryName = category.name,
                searchQuery = searchQuery
            )
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchBar()
        setupRetryButton()
        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = categoriesAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    if (lastVisibleItem >= totalItemCount - LOAD_MORE_THRESHOLD) {
                        viewModel.loadMoreCategories()
                    }
                }
            })
        }
    }

    private fun setupSearchBar() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onSearchQueryChanged(s?.toString() ?: "")
            }
        })
    }

    private fun setupRetryButton() {
        binding.buttonRetry.setOnClickListener {
            viewModel.loadCategories()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.progressBar.isVisible = state is UiState.Loading
                        binding.recyclerViewCategories.isVisible = state is UiState.Success
                        binding.layoutError.isVisible = state is UiState.Error

                        when (state) {
                            is UiState.Success -> categoriesAdapter.submitList(state.data)
                            is UiState.Error -> binding.textViewError.text = state.message
                            is UiState.Loading -> { /* handled by visibility */ }
                        }
                    }
                }
                launch {
                    viewModel.isLoadingMore.collect { isLoading ->
                        binding.progressBarLoadMore.isVisible = isLoading
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOAD_MORE_THRESHOLD = 2
    }
}
