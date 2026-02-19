package com.securetech.wallpapers.data.repository

import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    // Add API service or data source here when ready
) : WallpaperRepository {
    
    override suspend fun getCategories(): List<String> {
        // TODO: Implement actual data fetching
        return emptyList()
    }
    
    override suspend fun getWallpapersByCategory(category: String): List<String> {
        // TODO: Implement actual data fetching
        return emptyList()
    }
}
