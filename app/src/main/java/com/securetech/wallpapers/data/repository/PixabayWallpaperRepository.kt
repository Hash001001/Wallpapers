package com.securetech.wallpapers.data.repository

import com.securetech.wallpapers.BuildConfig
import com.securetech.wallpapers.data.remote.PixabayApiService
import com.securetech.wallpapers.domain.model.Category
import com.securetech.wallpapers.domain.model.Wallpaper
import com.securetech.wallpapers.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PixabayWallpaperRepository @Inject constructor(
    private val apiService: PixabayApiService
) : WallpaperRepository {

    private val apiKey = BuildConfig.PIXABAY_API_KEY

    private val categoryDefinitions = listOf(
        CategoryDefinition("1", "Nature", "nature landscape", "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=400"),
        CategoryDefinition("2", "Abstract", "abstract colorful", "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=400"),
        CategoryDefinition("3", "Architecture", "architecture building", "https://images.unsplash.com/photo-1486325212027-8081e485255e?w=400"),
        CategoryDefinition("4", "Space", "space galaxy stars", "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=400"),
        CategoryDefinition("5", "Animals", "animals wildlife", "https://images.unsplash.com/photo-1474511320723-9a56873571b7?w=400"),
        CategoryDefinition("6", "Ocean", "ocean sea beach", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400"),
        CategoryDefinition("7", "Mountains", "mountains peaks", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=400"),
        CategoryDefinition("8", "Cityscapes", "city skyline night", "https://images.unsplash.com/photo-1477959858617-67f85cf4f1df?w=400"),
        CategoryDefinition("9", "Flowers", "flowers bloom", "https://images.unsplash.com/photo-1490750967868-88aa4f44baee?w=400"),
        CategoryDefinition("10", "Sunset", "sunset sunrise sky", "https://images.unsplash.com/photo-1495616811223-4d98c6e9c869?w=400")
    )

    override fun getCategories(): Flow<List<Category>> = flow {
        val categories = categoryDefinitions.map { def ->
            Category(
                id = def.id,
                name = def.name,
                thumbnailUrl = def.thumbnailUrl
            )
        }
        emit(categories)
    }

    override fun getWallpapersByCategory(categoryId: String): Flow<List<Wallpaper>> = flow {
        val categoryDef = categoryDefinitions.find { it.id == categoryId }
        if (categoryDef == null) {
            emit(emptyList())
            return@flow
        }

        if (apiKey.isBlank()) {
            emit(emptyList())
            return@flow
        }

        try {
            val response = apiService.searchImages(
                apiKey = apiKey,
                query = categoryDef.searchQuery
            )

            val wallpapers = response.hits.map { image ->
                Wallpaper(
                    id = image.id.toString(),
                    imageUrl = image.largeImageUrl,
                    categoryId = categoryId
                )
            }
            emit(wallpapers)
        } catch (e: Exception) {
            throw Exception("Failed to load wallpapers: ${e.message}", e)
        }
    }

    private data class CategoryDefinition(
        val id: String,
        val name: String,
        val searchQuery: String,
        val thumbnailUrl: String
    )
}
