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

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var allCategories: List<Category> = emptyList()
    private var currentQuery: String = ""
    private var currentPage = 1
    private var hasMorePages = true

    init {
        loadCategories()
    }

    fun loadCategories() {
        currentPage = 1
        hasMorePages = true
        allCategories = emptyList()
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            wallpaperRepository.getCategoriesPaged(currentPage, PAGE_SIZE)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to load categories")
                }
                .collect { categories ->
                    hasMorePages = categories.size >= PAGE_SIZE
                    allCategories = categories
                    applyFilter(currentQuery)
                }
        }
    }

    fun loadMoreCategories() {
        if (_isLoadingMore.value || !hasMorePages) return
        viewModelScope.launch {
            _isLoadingMore.value = true
            wallpaperRepository.getCategoriesPaged(currentPage + 1, PAGE_SIZE)
                .catch { _ ->
                    _isLoadingMore.value = false
                }
                .collect { newCategories ->
                    hasMorePages = newCategories.size >= PAGE_SIZE
                    if (newCategories.isNotEmpty()) {
                        currentPage++
                        allCategories = allCategories + newCategories
                    }
                    _isLoadingMore.value = false
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
                id = "${SEARCH_CATEGORY_PREFIX}$query",
                name = query,
                thumbnailUrl = ""
            )
            listOf(searchCategory) + filtered
        }

        _uiState.value = UiState.Success(result)
    }

    companion object {
        const val SEARCH_CATEGORY_PREFIX = "search_"
        private const val PAGE_SIZE = 4
    }
}
