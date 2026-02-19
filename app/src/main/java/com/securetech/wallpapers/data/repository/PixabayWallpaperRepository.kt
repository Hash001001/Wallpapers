package com.securetech.wallpapers.data.repository

import com.securetech.wallpapers.BuildConfig
import com.securetech.wallpapers.data.remote.PixabayApiService
import com.securetech.wallpapers.domain.model.Category
import com.securetech.wallpapers.domain.model.Wallpaper
import com.securetech.wallpapers.domain.repository.WallpaperRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PixabayWallpaperRepository @Inject constructor(
    private val apiService: PixabayApiService
) : WallpaperRepository {

    private val apiKey = BuildConfig.PIXABAY_API_KEY

    private val categoryDefinitions = listOf(
        CategoryDefinition("1", "Nature", "nature landscape"),
        CategoryDefinition("2", "Abstract", "abstract colorful"),
        CategoryDefinition("3", "Architecture", "architecture building"),
        CategoryDefinition("4", "Space", "space galaxy stars"),
        CategoryDefinition("5", "Animals", "animals wildlife"),
        CategoryDefinition("6", "Ocean", "ocean sea beach"),
        CategoryDefinition("7", "Mountains", "mountains peaks"),
        CategoryDefinition("8", "Cityscapes", "city skyline night"),
        CategoryDefinition("9", "Flowers", "flowers bloom"),
        CategoryDefinition("10", "Sunset", "sunset sunrise sky")
    )

    override fun getCategories(): Flow<List<Category>> = flow {
        if (apiKey.isBlank()) {
            emit(emptyList())
            return@flow
        }

        try {
            val categories = coroutineScope {
                categoryDefinitions.map { def ->
                    async {
                        try {
                            val response = apiService.searchImages(
                                apiKey = apiKey,
                                query = def.searchQuery,
                                perPage = 3
                            )
                            val thumbnailUrl = response.hits.firstOrNull()?.webformatUrl ?: ""
                            Category(
                                id = def.id,
                                name = def.name,
                                thumbnailUrl = thumbnailUrl
                            )
                        } catch (e: Exception) {
                            Category(
                                id = def.id,
                                name = def.name,
                                thumbnailUrl = ""
                            )
                        }
                    }
                }.awaitAll()
            }
            emit(categories)
        } catch (e: Exception) {
            throw Exception("Failed to load categories: ${e.message}", e)
        }
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
        val searchQuery: String
    )
}
