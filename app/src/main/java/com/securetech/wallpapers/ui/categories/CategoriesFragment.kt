package com.securetech.wallpapers.ui.categories

import android.os.Bundle
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
import com.securetech.wallpapers.R
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
        val action = CategoriesFragmentDirections
            .actionCategoriesToWallpaperList(
                categoryId = category.id,
                categoryName = category.name
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
        setupRetryButton()
        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = categoriesAdapter
        }
    }

    private fun setupRetryButton() {
        binding.buttonRetry.setOnClickListener {
            viewModel.loadCategories()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
