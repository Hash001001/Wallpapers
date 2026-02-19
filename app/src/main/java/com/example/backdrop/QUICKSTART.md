# Quick Start Guide - Clean Architecture

## Package Structure Created ✅

```
com.example.backdrop/
├── data/                   (Data Layer)
│   ├── repository/        → Repository implementations
│   ├── model/            → DTOs, Entities
│   └── network/          → API services, Interceptors
│
├── domain/                (Domain Layer - Business Logic)
│   ├── repository/       → Repository interfaces
│   ├── usecase/          → Business logic/Use cases
│   └── model/            → Domain models
│
└── presentation/          (Presentation Layer - UI)
    ├── ui/               → Fragments, Activities
    ├── viewmodel/        → ViewModels
    └── adapter/          → RecyclerView adapters
```

## Next Steps

### 1. Start with Domain Layer (Business Logic)
Create your domain models and repository interfaces:

```kotlin
// domain/model/Wallpaper.kt
data class Wallpaper(
    val id: String,
    val imageUrl: String,
    val title: String,
    val author: String
)

// domain/repository/WallpaperRepository.kt
interface WallpaperRepository {
    suspend fun getWallpapers(): Result<List<Wallpaper>>
    suspend fun getWallpaperById(id: String): Result<Wallpaper>
}

// domain/usecase/GetWallpapersUseCase.kt
class GetWallpapersUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(): Result<List<Wallpaper>> {
        return repository.getWallpapers()
    }
}
```

### 2. Implement Data Layer
Create API services and repository implementations:

```kotlin
// data/network/WallpaperApiService.kt
interface WallpaperApiService {
    @GET("wallpapers")
    suspend fun getWallpapers(): List<WallpaperDto>
}

// data/model/WallpaperDto.kt
data class WallpaperDto(
    @SerializedName("id") val id: String,
    @SerializedName("url") val imageUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String
)

// data/repository/WallpaperRepositoryImpl.kt
class WallpaperRepositoryImpl @Inject constructor(
    private val apiService: WallpaperApiService
) : WallpaperRepository {
    override suspend fun getWallpapers(): Result<List<Wallpaper>> {
        return try {
            val dtos = apiService.getWallpapers()
            val wallpapers = dtos.map { it.toDomain() }
            Result.success(wallpapers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 3. Create Presentation Layer
Build ViewModels and UI components:

```kotlin
// presentation/viewmodel/WallpaperViewModel.kt
@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val getWallpapersUseCase: GetWallpapersUseCase
) : ViewModel() {
    
    private val _wallpapers = MutableLiveData<List<Wallpaper>>()
    val wallpapers: LiveData<List<Wallpaper>> = _wallpapers
    
    fun loadWallpapers() {
        viewModelScope.launch {
            getWallpapersUseCase().fold(
                onSuccess = { _wallpapers.value = it },
                onFailure = { /* Handle error */ }
            )
        }
    }
}

// presentation/ui/WallpaperFragment.kt
@AndroidEntryPoint
class WallpaperFragment : Fragment() {
    private val viewModel: WallpaperViewModel by viewModels()
    private lateinit var binding: FragmentWallpaperBinding
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.wallpapers.observe(viewLifecycleOwner) { wallpapers ->
            // Update UI
        }
        viewModel.loadWallpapers()
    }
}

// presentation/adapter/WallpaperAdapter.kt
class WallpaperAdapter : RecyclerView.Adapter<WallpaperViewHolder>() {
    private val wallpapers = mutableListOf<Wallpaper>()
    
    fun submitList(list: List<Wallpaper>) {
        wallpapers.clear()
        wallpapers.addAll(list)
        notifyDataSetChanged()
    }
}
```

### 4. Set up Dependency Injection
Create Hilt modules for dependencies:

```kotlin
// di/DataModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideWallpaperApiService(retrofit: Retrofit): WallpaperApiService {
        return retrofit.create(WallpaperApiService::class.java)
    }
}

// di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindWallpaperRepository(
        impl: WallpaperRepositoryImpl
    ): WallpaperRepository
}
```

## Benefits of This Structure

✅ **Separation of Concerns**: Each layer has a specific responsibility
✅ **Testability**: Easy to test each layer independently
✅ **Maintainability**: Changes in one layer don't affect others
✅ **Scalability**: Easy to add new features following the same pattern
✅ **Clean Dependencies**: Domain layer has no Android dependencies
✅ **Flexibility**: Easy to swap implementations (e.g., change from Retrofit to Ktor)

## Additional Resources

- Full documentation: See `README.md` in this directory
- Android Architecture Guide: https://developer.android.com/topic/architecture
- Clean Architecture: https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html

## Current Project Configuration

The project is already configured with:
- ✅ Hilt for dependency injection
- ✅ Retrofit for networking
- ✅ Coroutines for async operations
- ✅ ViewModel & LiveData
- ✅ ViewBinding
- ✅ Navigation Component

All you need to do is start adding your domain models, use cases, and implementations!
