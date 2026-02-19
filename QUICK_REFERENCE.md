# Quick Reference Card

## 🚀 Getting Started

### Run the App
```bash
./gradlew installDebug
```

### Build the Project
```bash
./gradlew build
```

## 📋 Key Files Reference

### Hilt Setup
```kotlin
// Application
@HiltAndroidApp
class WallpapersApplication : Application()

// Activity/Fragment
@AndroidEntryPoint
class MainActivity : AppCompatActivity()

// ViewModel
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel()

// Module (Provides)
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit { ... }
}

// Module (Binds)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(impl: Impl): Interface
}
```

### Navigation
```kotlin
// Navigate with arguments
findNavController().navigate(
    CategoriesFragmentDirections
        .actionCategoriesToWallpaperList(category = "Nature")
)

// Access arguments (Safe Args)
val args: WallpaperListFragmentArgs by navArgs()
val category = args.category
```

### ViewBinding
```kotlin
class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Use binding.viewName
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

## 🗂️ Project Structure

```
com.securetech.wallpapers/
├── WallpapersApplication      // @HiltAndroidApp
├── MainActivity               // @AndroidEntryPoint, Navigation
├── data/
│   └── repository/
│       ├── WallpaperRepository      // Interface
│       └── WallpaperRepositoryImpl  // @Inject
├── di/
│   ├── NetworkModule          // @Module (Retrofit, OkHttp)
│   └── RepositoryModule       // @Module (Repository binding)
└── ui/
    ├── categories/
    │   ├── CategoriesFragment       // @AndroidEntryPoint
    │   └── CategoriesViewModel      // @HiltViewModel
    ├── wallpaperlist/
    │   └── WallpaperListFragment    // @AndroidEntryPoint
    └── preview/
        └── PreviewFragment          // @AndroidEntryPoint
```

## 🧭 Navigation Graph

```
CategoriesFragment (start)
    ↓ action_categories_to_wallpaperList
WallpaperListFragment (arg: category)
    ↓ action_wallpaperList_to_preview
PreviewFragment (arg: wallpaperId)
```

## 🔧 Gradle Dependencies (Already Configured)

```kotlin
// Hilt
implementation(libs.hilt.android)
ksp(libs.hilt.compiler)

// Navigation
implementation(libs.androidx.navigation.fragment.ktx)
implementation(libs.androidx.navigation.ui.ktx)

// Lifecycle
implementation(libs.androidx.lifecycle.viewmodel.ktx)
implementation(libs.androidx.lifecycle.livedata.ktx)

// Networking
implementation(libs.retrofit)
implementation(libs.okhttp)
implementation(libs.gson)
```

## 📱 Fragments Overview

### CategoriesFragment
- **Purpose**: Display wallpaper categories
- **Layout**: RecyclerView with LinearLayoutManager
- **Navigation**: → WallpaperListFragment (with category)

### WallpaperListFragment  
- **Purpose**: Display wallpapers in a category
- **Layout**: RecyclerView with GridLayoutManager (2 cols)
- **Arguments**: category (String)
- **Navigation**: → PreviewFragment (with wallpaperId)

### PreviewFragment
- **Purpose**: Full-screen wallpaper preview
- **Layout**: ImageView + FloatingActionButton
- **Arguments**: wallpaperId (String)
- **Actions**: Download wallpaper

## 🎯 Next Implementation Steps

1. **Create API Service**
   ```kotlin
   interface WallpaperApiService {
       @GET("categories")
       suspend fun getCategories(): List<Category>
   }
   ```

2. **Add to NetworkModule**
   ```kotlin
   @Provides
   @Singleton
   fun provideApiService(retrofit: Retrofit): WallpaperApiService {
       return retrofit.create(WallpaperApiService::class.java)
   }
   ```

3. **Update Repository Implementation**
   ```kotlin
   class WallpaperRepositoryImpl @Inject constructor(
       private val apiService: WallpaperApiService
   ) : WallpaperRepository {
       override suspend fun getCategories() = 
           apiService.getCategories()
   }
   ```

4. **Create RecyclerView Adapters**
   - CategoriesAdapter
   - WallpapersAdapter

5. **Implement ViewModels**
   - Load data in ViewModel
   - Expose LiveData/StateFlow to Fragment
   - Handle loading states and errors

6. **Add Image Loading**
   ```kotlin
   // Add to build.gradle.kts
   implementation("io.coil-kt:coil:2.5.0")
   
   // Usage
   imageView.load(url) {
       crossfade(true)
       placeholder(R.drawable.placeholder)
   }
   ```

## 📖 Documentation Files

- `IMPLEMENTATION_SUMMARY.md` - Complete implementation details
- `HILT_NAVIGATION_SETUP.md` - Setup guide
- `NAVIGATION_DIAGRAM.md` - Visual flow diagrams
- `QUICK_REFERENCE.md` - This file

## ✅ All Requirements Met

✓ Hilt dependency injection configured  
✓ Network and Repository modules created  
✓ Example repository binding implemented  
✓ Navigation Component with 3 fragments  
✓ ViewBinding in all fragments  
✓ Proper lifecycle management  
✓ RecyclerView/ViewPager placeholders  
✓ No business logic (ready for implementation)  
✓ Kotlin used throughout  
✓ Gradle plugins configured  

## 🎉 Ready for Development!

The foundation is complete. Start implementing features by:
1. Adding data models
2. Creating API services
3. Implementing ViewModels
4. Building RecyclerView adapters
5. Adding image loading
6. Implementing business logic

Happy coding! 🚀
