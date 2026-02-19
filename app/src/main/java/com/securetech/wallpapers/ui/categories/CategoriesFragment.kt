package com.securetech.wallpapers.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.securetech.wallpapers.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    
    // Example of Hilt dependency injection in Fragment
    private val viewModel: CategoriesViewModel by viewModels()

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
        
        // Example: Use injected ViewModel
        viewModel.loadCategories()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            // TODO: Set adapter when ready
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
