package com.securetech.wallpapers.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securetech.wallpapers.domain.model.Category
import com.securetech.wallpapers.domain.repository.WallpaperRepository
import com.securetech.wallpapers.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Category>>> = _uiState.asStateFlow()

    private var allCategories: List<Category> = emptyList()
    private var currentQuery: String = ""

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            wallpaperRepository.getCategories()
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to load categories")
                }
                .collect { categories ->
                    allCategories = categories
                    applyFilter(currentQuery)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        currentQuery = query.trim()
        applyFilter(currentQuery)
    }

    private fun applyFilter(query: String) {
        if (query.isEmpty()) {
            _uiState.value = UiState.Success(allCategories)
            return
        }

        val filtered = allCategories.filter {
            it.name.contains(query, ignoreCase = true)
        }

        val hasExactMatch = allCategories.any {
            it.name.equals(query, ignoreCase = true)
        }

        val result = if (hasExactMatch) {
            filtered
        } else {
            val searchCategory = Category(
                id = "search_$query",
                name = query,
                thumbnailUrl = ""
            )
            listOf(searchCategory) + filtered
        }

        _uiState.value = UiState.Success(result)
    }
}
