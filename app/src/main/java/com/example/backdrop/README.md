# Clean Architecture Package Structure

This package follows the Clean Architecture principles for Android development.

## Package Overview: `com.example.backdrop`

### 1. Data Layer (`data/`)
The data layer contains all data-related logic and external dependencies.

#### Sub-packages:
- **`data/repository/`**: Concrete implementations of repository interfaces defined in the domain layer
  - Handles data operations from multiple sources (network, database, cache)
  - Implements repository contracts from domain layer
  
- **`data/model/`**: Data Transfer Objects (DTOs) and database entities
  - Network response models
  - Database entities
  - Mappers to convert between data models and domain models
  
- **`data/network/`**: Network-related classes
  - Retrofit API service interfaces
  - OkHttp interceptors
  - Network utilities

### 2. Domain Layer (`domain/`)
The domain layer contains business logic and is independent of Android framework and external dependencies.

#### Sub-packages:
- **`domain/repository/`**: Repository interfaces (contracts)
  - Defines the contract for data operations
  - No implementation details, only method signatures
  
- **`domain/usecase/`**: Use cases (Interactors)
  - Contains business logic
  - Single responsibility principle - one use case per class
  - Orchestrates data flow between repositories and presentation layer
  
- **`domain/model/`**: Domain models (Business entities)
  - Pure Kotlin data classes
  - Represents core business entities
  - Independent of data layer models

### 3. Presentation Layer (`presentation/`)
The presentation layer contains all UI-related code and follows MVVM pattern.

#### Sub-packages:
- **`presentation/ui/`**: UI components
  - Activities
  - Fragments
  - Custom Views
  - Feature-based organization (e.g., home/, detail/, search/)
  
- **`presentation/viewmodel/`**: ViewModels
  - Manages UI-related data
  - Handles UI logic and state
  - Communicates with use cases from domain layer
  - Exposes LiveData/StateFlow to UI
  
- **`presentation/adapter/`**: RecyclerView adapters
  - List adapters
  - ViewHolders
  - DiffUtil callbacks

## Clean Architecture Principles

1. **Dependency Rule**: Dependencies point inward
   - Presentation → Domain ← Data
   - Domain layer has no dependencies on other layers
   - Data and Presentation depend on Domain

2. **Separation of Concerns**: Each layer has a specific responsibility
   - Data: How to get/store data
   - Domain: What the app does (business logic)
   - Presentation: How to display data to user

3. **Testability**: 
   - Domain layer is easily unit testable
   - Data layer can be tested with mock APIs
   - Presentation layer can use fake use cases

## Example Usage

### Data Layer Example
```kotlin
// data/network/WallpaperApiService.kt
interface WallpaperApiService {
    @GET("wallpapers")
    suspend fun getWallpapers(): List<WallpaperDto>
}

// data/repository/WallpaperRepositoryImpl.kt
class WallpaperRepositoryImpl @Inject constructor(
    private val apiService: WallpaperApiService
) : WallpaperRepository {
    override suspend fun getWallpapers(): Result<List<Wallpaper>> {
        // Implementation
    }
}
```

### Domain Layer Example
```kotlin
// domain/repository/WallpaperRepository.kt
interface WallpaperRepository {
    suspend fun getWallpapers(): Result<List<Wallpaper>>
}

// domain/model/Wallpaper.kt
data class Wallpaper(
    val id: String,
    val url: String,
    val title: String
)

// domain/usecase/GetWallpapersUseCase.kt
class GetWallpapersUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(): Result<List<Wallpaper>> {
        return repository.getWallpapers()
    }
}
```

### Presentation Layer Example
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
            // Use case execution
        }
    }
}

// presentation/ui/WallpaperFragment.kt
@AndroidEntryPoint
class WallpaperFragment : Fragment() {
    private val viewModel: WallpaperViewModel by viewModels()
    // Fragment implementation
}

// presentation/adapter/WallpaperAdapter.kt
class WallpaperAdapter : RecyclerView.Adapter<WallpaperViewHolder>() {
    // Adapter implementation
}
```

## Dependencies & Dependency Injection

This structure is designed to work with:
- **Hilt** for dependency injection
- **Retrofit** for networking (data layer)
- **Coroutines & Flow** for asynchronous operations
- **ViewModel & LiveData** for presentation layer
- **ViewBinding** for view references

## Best Practices

1. **Keep domain layer pure**: No Android dependencies
2. **Use dependency injection**: Inject dependencies via constructors
3. **Single responsibility**: Each class should have one job
4. **Interface segregation**: Use small, focused interfaces
5. **Use cases for business logic**: Don't put business logic in ViewModels
6. **Map between layers**: Convert DTOs to domain models, domain models to UI models if needed
7. **Error handling**: Handle errors at appropriate layers
8. **Testing**: Write tests for each layer independently

## References

- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Guide to app architecture](https://developer.android.com/jetpack/guide)
