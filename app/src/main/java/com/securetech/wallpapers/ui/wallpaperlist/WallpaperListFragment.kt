package com.securetech.wallpapers.ui.wallpaperlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.securetech.wallpapers.databinding.FragmentWallpaperListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallpaperListFragment : Fragment() {

    private var _binding: FragmentWallpaperListBinding? = null
    private val binding get() = _binding!!
    
    private val args: WallpaperListFragmentArgs by navArgs()

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
        
        // Access navigation arguments
        val category = args.category
        // TODO: Load wallpapers for this category
    }

    private fun setupRecyclerView() {
        binding.recyclerViewWallpapers.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            // TODO: Set adapter when ready
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
