package com.securetech.wallpapers.ui.preview

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.securetech.wallpapers.domain.model.Wallpaper
import com.securetech.wallpapers.domain.repository.WallpaperRepository
import com.securetech.wallpapers.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String = savedStateHandle["categoryId"] ?: ""
    private val searchQuery: String = savedStateHandle["searchQuery"] ?: ""
    val initialIndex: Int = savedStateHandle["wallpaperIndex"] ?: 0

    private val _uiState = MutableStateFlow<UiState<List<Wallpaper>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Wallpaper>>> = _uiState.asStateFlow()

    private val _actionState = MutableSharedFlow<ActionResult>()
    val actionState: SharedFlow<ActionResult> = _actionState.asSharedFlow()

    init {
        loadWallpapers()
    }

    private fun loadWallpapers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val flow = if (searchQuery.isNotBlank()) {
                wallpaperRepository.searchWallpapers(searchQuery)
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

    fun downloadWallpaper(imageUrl: String) {
        viewModelScope.launch {
            _actionState.emit(ActionResult.Loading("Downloading..."))
            try {
                val bitmap = loadBitmap(imageUrl) ?: throw IOException("Failed to load image")
                saveBitmapToGallery(bitmap)
                _actionState.emit(ActionResult.Success("Wallpaper saved to Downloads"))
            } catch (e: Exception) {
                _actionState.emit(ActionResult.Error(e.message ?: "Download failed"))
            }
        }
    }

    fun setWallpaper(imageUrl: String, target: WallpaperTarget) {
        viewModelScope.launch {
            _actionState.emit(ActionResult.Loading("Setting wallpaper..."))
            try {
                val bitmap = loadBitmap(imageUrl) ?: throw IOException("Failed to load image")
                val wallpaperManager = WallpaperManager.getInstance(context)
                withContext(Dispatchers.IO) {
                    when (target) {
                        WallpaperTarget.HOME ->
                            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                        WallpaperTarget.LOCK ->
                            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                        WallpaperTarget.BOTH ->
                            wallpaperManager.setBitmap(bitmap)
                    }
                }
                _actionState.emit(ActionResult.Success("Wallpaper set successfully"))
            } catch (e: Exception) {
                _actionState.emit(ActionResult.Error(e.message ?: "Failed to set wallpaper"))
            }
        }
    }

    private suspend fun loadBitmap(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val result = loader.execute(request)
            (result as? SuccessResult)?.drawable?.let { drawable ->
                val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.RGB_565
                )
                val canvas = android.graphics.Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
        }
    }

    private suspend fun saveBitmapToGallery(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val filename = "wallpaper_${System.currentTimeMillis()}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw IOException("Failed to create media store entry")

            resolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
            } ?: throw IOException("Failed to open output stream")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }
        }
    }

    sealed class ActionResult {
        data class Loading(val message: String) : ActionResult()
        data class Success(val message: String) : ActionResult()
        data class Error(val message: String) : ActionResult()
    }

    enum class WallpaperTarget {
        HOME, LOCK, BOTH
    }
}
