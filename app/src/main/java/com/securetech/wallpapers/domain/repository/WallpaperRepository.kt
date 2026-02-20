package com.securetech.wallpapers.domain.repository

import com.securetech.wallpapers.domain.model.Category
import com.securetech.wallpapers.domain.model.Wallpaper
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    fun getCategories(): Flow<List<Category>>
    fun getWallpapersByCategory(categoryId: String): Flow<List<Wallpaper>>
    fun searchWallpapers(query: String): Flow<List<Wallpaper>>

    fun getCategoriesPaged(page: Int, pageSize: Int): Flow<List<Category>> = getCategories()
    fun getWallpapersByCategoryPaged(categoryId: String, page: Int, pageSize: Int): Flow<List<Wallpaper>> = getWallpapersByCategory(categoryId)
    fun searchWallpapersPaged(query: String, page: Int, pageSize: Int): Flow<List<Wallpaper>> = searchWallpapers(query)
}
