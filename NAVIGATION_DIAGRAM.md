# Navigation Flow Diagram

## Screen Navigation

```
┌─────────────────────────────────────────────────────────────────┐
│                      MainActivity                                │
│  (@AndroidEntryPoint, ViewBinding)                              │
│                                                                   │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │                    NavHostFragment                          │ │
│  │                  (navigation_graph.xml)                     │ │
│  │                                                              │ │
│  │  ┌──────────────────────────────────────────────────────┐  │ │
│  │  │         CategoriesFragment (Start)                    │  │ │
│  │  │  - RecyclerView (LinearLayoutManager)                 │  │ │
│  │  │  - Injected ViewModel                                 │  │ │
│  │  │  - Shows list of wallpaper categories                 │  │ │
│  │  └────────────┬─────────────────────────────────────────┘  │ │
│  │               │ action_categories_to_wallpaperList          │ │
│  │               │ (slide animation)                            │ │
│  │               ▼                                              │ │
│  │  ┌──────────────────────────────────────────────────────┐  │ │
│  │  │         WallpaperListFragment                         │  │ │
│  │  │  - RecyclerView (GridLayoutManager, 2 columns)        │  │ │
│  │  │  - Receives: category (String)                        │  │ │
│  │  │  - Shows wallpapers for selected category            │  │ │
│  │  └────────────┬─────────────────────────────────────────┘  │ │
│  │               │ action_wallpaperList_to_preview             │ │
│  │               │ (fade animation)                             │ │
│  │               ▼                                              │ │
│  │  ┌──────────────────────────────────────────────────────┐  │ │
│  │  │         PreviewFragment                               │  │ │
│  │  │  - ImageView (full screen preview)                    │  │ │
│  │  │  - FloatingActionButton (download)                    │  │ │
│  │  │  - Receives: wallpaperId (String)                     │  │ │
│  │  │  - Shows full wallpaper preview                       │  │ │
│  │  └──────────────────────────────────────────────────────┘  │ │
│  │                                                              │ │
│  └──────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## Dependency Injection Flow

```
┌─────────────────────────────────────────────────────────────────┐
│              WallpapersApplication                               │
│              (@HiltAndroidApp)                                   │
└─────────────────────┬───────────────────────────────────────────┘
                      │
                      │ Provides Hilt Components
                      │
        ┌─────────────┴─────────────┐
        │                           │
        ▼                           ▼
┌───────────────────┐       ┌──────────────────┐
│  NetworkModule    │       │ RepositoryModule │
│  (@Module)        │       │ (@Module)        │
│                   │       │                  │
│  Provides:        │       │  Binds:          │
│  - Gson           │       │  - Repository    │
│  - OkHttpClient   │──────▶│    Interface     │
│  - Retrofit       │       │    to Impl       │
│  - Interceptor    │       │                  │
└───────────────────┘       └────────┬─────────┘
                                     │
                                     │ Injected into
                                     ▼
                    ┌─────────────────────────────────┐
                    │  WallpaperRepositoryImpl        │
                    │  (@Inject constructor)          │
                    └────────────┬────────────────────┘
                                 │
                                 │ Injected into
                                 ▼
                    ┌─────────────────────────────────┐
                    │  CategoriesViewModel            │
                    │  (@HiltViewModel)               │
                    └────────────┬────────────────────┘
                                 │
                                 │ Injected into
                                 ▼
                    ┌─────────────────────────────────┐
                    │  CategoriesFragment             │
                    │  (@AndroidEntryPoint)           │
                    │  private val viewModel by       │
                    │       viewModels()              │
                    └─────────────────────────────────┘
```

## Fragment Lifecycle with ViewBinding

```
Fragment Lifecycle               ViewBinding Lifecycle
─────────────────               ─────────────────────

onCreate()
    │
    ▼
onCreateView() ────────────────▶ binding = FragmentXxxBinding.inflate()
    │                            _binding = binding
    ▼                            return binding.root
onViewCreated()
    │                            Use: binding.viewName
    ▼
onStart()
    │
    ▼
onResume()
    │
    .
    .
    .
    │
    ▼
onPause()
    │
    ▼
onStop()
    │
    ▼
onDestroyView() ───────────────▶ _binding = null (Clean up!)
    │
    ▼
onDestroy()
```

## Key Annotations

### Hilt Annotations
- `@HiltAndroidApp` - Application class
- `@AndroidEntryPoint` - Activities and Fragments that use injection
- `@HiltViewModel` - ViewModels with constructor injection
- `@Module` - Hilt modules that provide dependencies
- `@InstallIn(SingletonComponent::class)` - Module lifecycle scope
- `@Provides` - Methods that provide dependencies
- `@Binds` - Bind interface to implementation
- `@Singleton` - Singleton scope for dependencies
- `@Inject` - Constructor injection

### Navigation
- Safe Args generates these classes:
  - `WallpaperListFragmentArgs` - Access arguments in WallpaperListFragment
  - `PreviewFragmentArgs` - Access arguments in PreviewFragment
  - `CategoriesFragmentDirections` - Navigate from CategoriesFragment
  - `WallpaperListFragmentDirections` - Navigate from WallpaperListFragment

## File Structure

```
app/src/main/
├── java/com/securetech/wallpapers/
│   ├── WallpapersApplication.kt          # @HiltAndroidApp
│   ├── MainActivity.kt                   # @AndroidEntryPoint, Navigation setup
│   ├── data/
│   │   └── repository/
│   │       ├── WallpaperRepository.kt    # Interface
│   │       └── WallpaperRepositoryImpl.kt # @Inject constructor
│   ├── di/
│   │   ├── NetworkModule.kt              # @Module for network
│   │   └── RepositoryModule.kt           # @Module for repositories
│   └── ui/
│       ├── categories/
│       │   ├── CategoriesFragment.kt     # @AndroidEntryPoint
│       │   └── CategoriesViewModel.kt    # @HiltViewModel
│       ├── wallpaperlist/
│       │   └── WallpaperListFragment.kt  # @AndroidEntryPoint
│       └── preview/
│           └── PreviewFragment.kt        # @AndroidEntryPoint
└── res/
    ├── layout/
    │   ├── activity_main.xml             # NavHostFragment
    │   ├── fragment_categories.xml       # RecyclerView
    │   ├── fragment_wallpaper_list.xml   # RecyclerView (Grid)
    │   └── fragment_preview.xml          # ImageView + FAB
    └── navigation/
        └── navigation_graph.xml          # Navigation graph
```
