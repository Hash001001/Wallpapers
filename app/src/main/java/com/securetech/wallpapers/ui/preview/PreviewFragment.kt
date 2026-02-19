package com.securetech.wallpapers.ui.preview

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.securetech.wallpapers.R
import com.securetech.wallpapers.databinding.FragmentPreviewBinding
import com.securetech.wallpapers.ui.UiState
import com.securetech.wallpapers.ui.adapter.PreviewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreviewFragment : Fragment() {

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreviewViewModel by viewModels()
    private val pagerAdapter = PreviewPagerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupButtons()
        observeUiState()
        observeActionState()
    }

    private fun setupViewPager() {
        binding.viewPagerPreview.adapter = pagerAdapter
    }

    private fun setupButtons() {
        binding.fabDownload.setOnClickListener {
            val currentUrl = getCurrentWallpaperUrl() ?: return@setOnClickListener
            viewModel.downloadWallpaper(currentUrl)
        }

        binding.fabSetWallpaper.setOnClickListener {
            val currentUrl = getCurrentWallpaperUrl() ?: return@setOnClickListener
            showSetWallpaperDialog(currentUrl)
        }
    }

    private fun getCurrentWallpaperUrl(): String? {
        val position = binding.viewPagerPreview.currentItem
        return pagerAdapter.getItem(position)?.imageUrl
    }

    private fun showSetWallpaperDialog(imageUrl: String) {
        val options = arrayOf(
            getString(R.string.set_home_screen),
            getString(R.string.set_lock_screen),
            getString(R.string.set_both)
        )

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.choose_wallpaper_target)
            .setItems(options) { _, which ->
                val target = when (which) {
                    0 -> PreviewViewModel.WallpaperTarget.HOME
                    1 -> PreviewViewModel.WallpaperTarget.LOCK
                    else -> PreviewViewModel.WallpaperTarget.BOTH
                }
                viewModel.setWallpaper(imageUrl, target)
            }
            .show()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state is UiState.Loading
                    binding.viewPagerPreview.isVisible = state is UiState.Success
                    binding.fabDownload.isVisible = state is UiState.Success
                    binding.fabSetWallpaper.isVisible = state is UiState.Success

                    when (state) {
                        is UiState.Success -> {
                            pagerAdapter.submitList(state.data)
                            binding.viewPagerPreview.setCurrentItem(
                                viewModel.initialIndex, false
                            )
                        }
                        is UiState.Error -> {
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                        is UiState.Loading -> { /* handled by visibility */ }
                    }
                }
            }
        }
    }

    private fun observeActionState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actionState.collect { result ->
                    when (result) {
                        is PreviewViewModel.ActionResult.Loading ->
                            Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                        is PreviewViewModel.ActionResult.Success ->
                            Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                        is PreviewViewModel.ActionResult.Error ->
                            Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
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
