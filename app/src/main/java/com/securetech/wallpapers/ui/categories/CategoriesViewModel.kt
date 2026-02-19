package com.securetech.wallpapers.ui.categories

import androidx.lifecycle.ViewModel
import com.securetech.wallpapers.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Example ViewModel demonstrating Hilt dependency injection
 * The repository is automatically injected by Hilt
 */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : ViewModel() {

    // TODO: Implement ViewModel logic
    // Example: Load categories from repository
    fun loadCategories() {
        // Use wallpaperRepository here
    }
}
