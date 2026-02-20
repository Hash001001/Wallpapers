package com.securetech.wallpapers.ui.wallpaperlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securetech.wallpapers.domain.model.Wallpaper
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
class WallpaperListViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String = savedStateHandle["categoryId"] ?: ""
    private val initialSearchQuery: String = savedStateHandle["searchQuery"] ?: ""

    private val _uiState = MutableStateFlow<UiState<List<Wallpaper>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Wallpaper>>> = _uiState.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var allWallpapers: List<Wallpaper> = emptyList()
    private var activeQuery: String = initialSearchQuery
    val activeSearchQuery: String get() = activeQuery
    private var currentPage = 1
    private var hasMorePages = true

    init {
        loadWallpapers()
    }

    fun loadWallpapers() {
        currentPage = 1
        hasMorePages = true
        allWallpapers = emptyList()
        activeQuery = initialSearchQuery
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val flow = if (initialSearchQuery.isNotBlank()) {
                wallpaperRepository.searchWallpapersPaged(initialSearchQuery, currentPage, PAGE_SIZE)
            } else {
                wallpaperRepository.getWallpapersByCategoryPaged(categoryId, currentPage, PAGE_SIZE)
            }
            flow.catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to load wallpapers")
                }
                .collect { wallpapers ->
                    hasMorePages = wallpapers.size >= PAGE_SIZE
                    allWallpapers = wallpapers
                    _uiState.value = UiState.Success(allWallpapers)
                }
        }
    }

    fun loadMoreWallpapers() {
        if (_isLoadingMore.value || !hasMorePages) return
        viewModelScope.launch {
            _isLoadingMore.value = true
            val flow = if (activeQuery.isNotBlank()) {
                wallpaperRepository.searchWallpapersPaged(activeQuery, currentPage + 1, PAGE_SIZE)
            } else {
                wallpaperRepository.getWallpapersByCategoryPaged(categoryId, currentPage + 1, PAGE_SIZE)
            }
            flow.catch { _ ->
                    _isLoadingMore.value = false
                }
                .collect { newWallpapers ->
                    hasMorePages = newWallpapers.size >= PAGE_SIZE
                    if (newWallpapers.isNotEmpty()) {
                        currentPage++
                        allWallpapers = allWallpapers + newWallpapers
                        _uiState.value = UiState.Success(allWallpapers)
                    }
                    _isLoadingMore.value = false
                }
        }
    }

    fun searchWallpapers(query: String) {
        val trimmed = query.trim()
        activeQuery = trimmed
        currentPage = 1
        hasMorePages = true
        allWallpapers = emptyList()
        if (trimmed.isBlank()) {
            loadWallpapers()
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            wallpaperRepository.searchWallpapersPaged(trimmed, currentPage, PAGE_SIZE)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to search wallpapers")
                }
                .collect { wallpapers ->
                    hasMorePages = wallpapers.size >= PAGE_SIZE
                    allWallpapers = wallpapers
                    _uiState.value = UiState.Success(allWallpapers)
                }
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
