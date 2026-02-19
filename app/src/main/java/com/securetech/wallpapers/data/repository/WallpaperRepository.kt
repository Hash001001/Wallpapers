package com.securetech.wallpapers.data.repository

interface WallpaperRepository {
    suspend fun getCategories(): List<String>
    suspend fun getWallpapersByCategory(category: String): List<String>
}
