package com.securetech.wallpapers.ui.wallpaperlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.securetech.wallpapers.databinding.FragmentWallpaperListBinding
import com.securetech.wallpapers.ui.UiState
import com.securetech.wallpapers.ui.adapter.WallpapersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WallpaperListFragment : Fragment() {

    private var _binding: FragmentWallpaperListBinding? = null
    private val binding get() = _binding!!

    private val args: WallpaperListFragmentArgs by navArgs()
    private val viewModel: WallpaperListViewModel by viewModels()

    private val wallpapersAdapter = WallpapersAdapter { position ->
        val action = WallpaperListFragmentDirections
            .actionWallpaperListToPreview(
                categoryId = args.categoryId,
                wallpaperIndex = position,
                searchQuery = viewModel.activeSearchQuery
            )
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperListBinding.inflate(inflater, container, false)
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
        binding.recyclerViewWallpapers.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = wallpapersAdapter
        }
    }

    private fun setupSearchBar() {
        binding.editTextSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchWallpapers(v.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun setupRetryButton() {
        binding.buttonRetry.setOnClickListener {
            viewModel.loadWallpapers()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state is UiState.Loading
                    binding.recyclerViewWallpapers.isVisible = state is UiState.Success
                    binding.layoutError.isVisible = state is UiState.Error
                    binding.textViewEmpty.isVisible = state is UiState.Success && state.data.isEmpty()

                    when (state) {
                        is UiState.Success -> wallpapersAdapter.submitList(state.data)
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
