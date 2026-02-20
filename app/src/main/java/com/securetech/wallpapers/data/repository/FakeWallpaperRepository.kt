package com.securetech.wallpapers.data.repository

import com.securetech.wallpapers.domain.model.Category
import com.securetech.wallpapers.domain.model.Wallpaper
import com.securetech.wallpapers.domain.repository.WallpaperRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeWallpaperRepository @Inject constructor() : WallpaperRepository {

    private val categories = listOf(
        Category("1", "Nature", "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=400"),
        Category("2", "Abstract", "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=400"),
        Category("3", "Architecture", "https://images.unsplash.com/photo-1486325212027-8081e485255e?w=400"),
        Category("4", "Space", "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=400"),
        Category("5", "Animals", "https://images.unsplash.com/photo-1474511320723-9a56873571b7?w=400"),
        Category("6", "Ocean", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400"),
        Category("7", "Mountains", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=400"),
        Category("8", "Cityscapes", "https://images.unsplash.com/photo-1477959858617-67f85cf4f1df?w=400")
    )

    private val extraCategories = listOf(
        Category("9", "Forest", "https://images.unsplash.com/photo-1448375240586-882707db888b?w=400"),
        Category("10", "Desert", "https://images.unsplash.com/photo-1509316785289-025f5b846b35?w=400"),
        Category("11", "Waterfall", "https://images.unsplash.com/photo-1432405972618-c6b0cfba8673?w=400"),
        Category("12", "Winter", "https://images.unsplash.com/photo-1457269449834-928af64c684d?w=400"),
        Category("13", "Autumn", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400"),
        Category("14", "Cars", "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=400"),
        Category("15", "Food", "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400"),
        Category("16", "Music", "https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=400")
    )

    private val allCategories: List<Category> by lazy {
        categories + extraCategories.shuffled()
    }

    private val wallpapers = listOf(
        // Nature
        Wallpaper("n1", "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=1080", "1"),
        Wallpaper("n2", "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=1080", "1"),
        Wallpaper("n3", "https://images.unsplash.com/photo-1433086966358-54859d0ed716?w=1080", "1"),
        Wallpaper("n4", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=1080", "1"),
        Wallpaper("n5", "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=1080", "1"),
        Wallpaper("n6", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?w=1080", "1"),
        // Abstract
        Wallpaper("a1", "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=1080", "2"),
        Wallpaper("a2", "https://images.unsplash.com/photo-1550859492-d5da9d8e45f3?w=1080", "2"),
        Wallpaper("a3", "https://images.unsplash.com/photo-1567095761054-7a02e69e5b2b?w=1080", "2"),
        Wallpaper("a4", "https://images.unsplash.com/photo-1553356084-58ef4a67b2a7?w=1080", "2"),
        Wallpaper("a5", "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?w=1080", "2"),
        // Architecture
        Wallpaper("ar1", "https://images.unsplash.com/photo-1486325212027-8081e485255e?w=1080", "3"),
        Wallpaper("ar2", "https://images.unsplash.com/photo-1487958449943-2429e8be8625?w=1080", "3"),
        Wallpaper("ar3", "https://images.unsplash.com/photo-1448630360428-65456885c650?w=1080", "3"),
        Wallpaper("ar4", "https://images.unsplash.com/photo-1431576901776-e539bd916ba2?w=1080", "3"),
        // Space
        Wallpaper("s1", "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=1080", "4"),
        Wallpaper("s2", "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=1080", "4"),
        Wallpaper("s3", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1080", "4"),
        Wallpaper("s4", "https://images.unsplash.com/photo-1419242902214-272b3f66ee7a?w=1080", "4"),
        Wallpaper("s5", "https://images.unsplash.com/photo-1464802686167-b939a6910659?w=1080", "4"),
        // Animals
        Wallpaper("an1", "https://images.unsplash.com/photo-1474511320723-9a56873571b7?w=1080", "5"),
        Wallpaper("an2", "https://images.unsplash.com/photo-1437622368342-7a3d73a34c8f?w=1080", "5"),
        Wallpaper("an3", "https://images.unsplash.com/photo-1425082661705-1834bfd09dca?w=1080", "5"),
        Wallpaper("an4", "https://images.unsplash.com/photo-1484406566174-2da7ec519411?w=1080", "5"),
        // Ocean
        Wallpaper("o1", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1080", "6"),
        Wallpaper("o2", "https://images.unsplash.com/photo-1505118380757-91f5f5632de0?w=1080", "6"),
        Wallpaper("o3", "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=1080", "6"),
        Wallpaper("o4", "https://images.unsplash.com/photo-1471922694854-ff1b63b20054?w=1080", "6"),
        // Mountains
        Wallpaper("m1", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=1080", "7"),
        Wallpaper("m2", "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=1080", "7"),
        Wallpaper("m3", "https://images.unsplash.com/photo-1454496522488-7a8e488e8606?w=1080", "7"),
        Wallpaper("m4", "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=1080", "7"),
        // Cityscapes
        Wallpaper("c1", "https://images.unsplash.com/photo-1477959858617-67f85cf4f1df?w=1080", "8"),
        Wallpaper("c2", "https://images.unsplash.com/photo-1449824913935-59a10b8d2000?w=1080", "8"),
        Wallpaper("c3", "https://images.unsplash.com/photo-1480714378408-67cf0d13bc1b?w=1080", "8"),
        Wallpaper("c4", "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=1080", "8")
    )

    override fun getCategories(): Flow<List<Category>> = flow {
        delay(500)
        emit(categories)
    }

    override fun getCategoriesPaged(page: Int, pageSize: Int): Flow<List<Category>> = flow {
        delay(500)
        val fromIndex = (page - 1) * pageSize
        if (fromIndex >= allCategories.size) {
            emit(emptyList())
        } else {
            emit(allCategories.subList(fromIndex, minOf(fromIndex + pageSize, allCategories.size)))
        }
    }

    override fun getWallpapersByCategory(categoryId: String): Flow<List<Wallpaper>> = flow {
        delay(300)
        emit(wallpapers.filter { it.categoryId == categoryId })
    }

    override fun getWallpapersByCategoryPaged(categoryId: String, page: Int, pageSize: Int): Flow<List<Wallpaper>> = flow {
        delay(300)
        val filtered = wallpapers.filter { it.categoryId == categoryId }
        val fromIndex = (page - 1) * pageSize
        if (fromIndex >= filtered.size) {
            emit(emptyList())
        } else {
            emit(filtered.subList(fromIndex, minOf(fromIndex + pageSize, filtered.size)))
        }
    }

    override fun searchWallpapers(query: String): Flow<List<Wallpaper>> = flow {
        delay(300)
        emit(wallpapers.filter { it.categoryId.isNotEmpty() })
    }

    override fun searchWallpapersPaged(query: String, page: Int, pageSize: Int): Flow<List<Wallpaper>> = flow {
        delay(300)
        val filtered = wallpapers.filter { it.categoryId.isNotEmpty() }
        val fromIndex = (page - 1) * pageSize
        if (fromIndex >= filtered.size) {
            emit(emptyList())
        } else {
            emit(filtered.subList(fromIndex, minOf(fromIndex + pageSize, filtered.size)))
        }
    }
}
