package com.securetech.wallpapers.data.repository

import com.securetech.wallpapers.BuildConfig
import com.securetech.wallpapers.data.remote.PixabayApiService
import com.securetech.wallpapers.domain.model.Category
import com.securetech.wallpapers.domain.model.Wallpaper
import com.securetech.wallpapers.domain.repository.WallpaperRepository
import kotlinx.coroutines.CancellationException
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
                                perPage = CATEGORY_THUMBNAIL_FETCH_COUNT
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to load categories: ${e.message}", e)
        }
    }

    override fun getCategoriesPaged(page: Int, pageSize: Int): Flow<List<Category>> = flow {
        if (apiKey.isBlank()) {
            emit(emptyList())
            return@flow
        }

        val fromIndex = (page - 1) * pageSize
        if (fromIndex >= categoryDefinitions.size) {
            emit(emptyList())
            return@flow
        }

        val pagedDefinitions = categoryDefinitions.subList(
            fromIndex,
            minOf(fromIndex + pageSize, categoryDefinitions.size)
        )

        try {
            val categories = coroutineScope {
                pagedDefinitions.map { def ->
                    async {
                        try {
                            val response = apiService.searchImages(
                                apiKey = apiKey,
                                query = def.searchQuery,
                                perPage = CATEGORY_THUMBNAIL_FETCH_COUNT
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
        } catch (e: CancellationException) {
            throw e
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to load wallpapers: ${e.message}", e)
        }
    }

    override fun getWallpapersByCategoryPaged(categoryId: String, page: Int, pageSize: Int): Flow<List<Wallpaper>> = flow {
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
                query = categoryDef.searchQuery,
                perPage = pageSize,
                page = page
            )

            val wallpapers = response.hits.map { image ->
                Wallpaper(
                    id = image.id.toString(),
                    imageUrl = image.largeImageUrl,
                    categoryId = categoryId
                )
            }
            emit(wallpapers)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to load wallpapers: ${e.message}", e)
        }
    }

    override fun searchWallpapers(query: String): Flow<List<Wallpaper>> = flow {
        if (apiKey.isBlank() || query.isBlank()) {
            emit(emptyList())
            return@flow
        }

        try {
            val response = apiService.searchImages(
                apiKey = apiKey,
                query = query,
                perPage = 20
            )

            val wallpapers = response.hits.map { image ->
                Wallpaper(
                    id = image.id.toString(),
                    imageUrl = image.largeImageUrl,
                    categoryId = ""
                )
            }
            emit(wallpapers)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to search wallpapers: ${e.message}", e)
        }
    }

    override fun searchWallpapersPaged(query: String, page: Int, pageSize: Int): Flow<List<Wallpaper>> = flow {
        if (apiKey.isBlank() || query.isBlank()) {
            emit(emptyList())
            return@flow
        }

        try {
            val response = apiService.searchImages(
                apiKey = apiKey,
                query = query,
                perPage = pageSize,
                page = page
            )

            val wallpapers = response.hits.map { image ->
                Wallpaper(
                    id = image.id.toString(),
                    imageUrl = image.largeImageUrl,
                    categoryId = ""
                )
            }
            emit(wallpapers)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to search wallpapers: ${e.message}", e)
        }
    }

    private data class CategoryDefinition(
        val id: String,
        val name: String,
        val searchQuery: String
    )

    companion object {
        private const val CATEGORY_THUMBNAIL_FETCH_COUNT = 3
    }
}
