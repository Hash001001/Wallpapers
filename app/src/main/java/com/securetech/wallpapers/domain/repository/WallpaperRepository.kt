package com.securetech.wallpapers.domain.repository

import com.securetech.wallpapers.domain.model.Category
import com.securetech.wallpapers.domain.model.Wallpaper
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    fun getCategories(): Flow<List<Category>>
    fun getWallpapersByCategory(categoryId: String): Flow<List<Wallpaper>>
}
