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

    private var activeQuery: String = initialSearchQuery
    val activeSearchQuery: String get() = activeQuery

    init {
        loadWallpapers()
    }

    fun loadWallpapers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val flow = if (initialSearchQuery.isNotBlank()) {
                wallpaperRepository.searchWallpapers(initialSearchQuery)
            } else {
                wallpaperRepository.getWallpapersByCategory(categoryId)
            }
            flow.catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to load wallpapers")
                }
                .collect { wallpapers ->
                    _uiState.value = UiState.Success(wallpapers)
                }
        }
    }

    fun searchWallpapers(query: String) {
        val trimmed = query.trim()
        activeQuery = trimmed
        if (trimmed.isBlank()) {
            loadWallpapers()
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            wallpaperRepository.searchWallpapers(trimmed)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to search wallpapers")
                }
                .collect { wallpapers ->
                    _uiState.value = UiState.Success(wallpapers)
                }
        }
    }
}
