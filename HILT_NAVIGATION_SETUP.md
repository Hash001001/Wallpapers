# Hilt Dependency Injection and Navigation Setup

## Overview
This document describes the Hilt dependency injection and Jetpack Navigation Component implementation for the Wallpapers application.

## Hilt Dependency Injection

### 1. Application Class
**File:** `WallpapersApplication.kt`
- Annotated with `@HiltAndroidApp` to enable Hilt in the application
- Registered in `AndroidManifest.xml` with `android:name=".WallpapersApplication"`

### 2. Dependency Injection Modules

#### NetworkModule (`di/NetworkModule.kt`)
Provides network-related dependencies:
- **Gson**: JSON parser with lenient configuration
- **HttpLoggingInterceptor**: For logging HTTP requests/responses (BODY level)
- **OkHttpClient**: HTTP client with 30-second timeouts and logging
- **Retrofit**: REST client with Gson converter factory

All dependencies are provided as Singletons.

#### RepositoryModule (`di/RepositoryModule.kt`)
Provides repository dependencies:
- **WallpaperRepository**: Interface binding to WallpaperRepositoryImpl
- Uses `@Binds` annotation for interface-to-implementation binding

### 3. Example Repository
**Files:**
- `data/repository/WallpaperRepository.kt` - Repository interface
- `data/repository/WallpaperRepositoryImpl.kt` - Repository implementation with `@Inject` constructor

### 4. Sample Injection Usage
**File:** `ui/categories/CategoriesViewModel.kt`
- Demonstrates `@HiltViewModel` annotation
- Shows constructor injection of `WallpaperRepository`
- Used in `CategoriesFragment` with `by viewModels()` delegate

## Jetpack Navigation Component

### 1. Navigation Graph
**File:** `res/navigation/navigation_graph.xml`
- Start destination: `CategoriesFragment`
- Three fragments configured with navigation actions:
  - **CategoriesFragment** → **WallpaperListFragment** (slide animation)
  - **WallpaperListFragment** → **PreviewFragment** (fade animation)
- Navigation arguments:
  - `WallpaperListFragment`: receives `category` (String)
  - `PreviewFragment`: receives `wallpaperId` (String)

### 2. NavHostFragment Setup
**File:** `res/layout/activity_main.xml`
- Uses `FragmentContainerView` with `NavHostFragment`
- Set as default nav host with `app:defaultNavHost="true"`
- References navigation graph: `app:navGraph="@navigation/navigation_graph"`

### 3. MainActivity Configuration
**File:** `MainActivity.kt`
- Annotated with `@AndroidEntryPoint` for Hilt
- Uses ViewBinding (`ActivityMainBinding`)
- Configures navigation with:
  - NavController from NavHostFragment
  - AppBarConfiguration with top-level destination (CategoriesFragment)
  - Action bar integration with `setupActionBarWithNavController()`
  - Up navigation support in `onSupportNavigateUp()`

## Fragments

### CategoriesFragment
**Location:** `ui/categories/`
- **Features:**
  - ViewBinding (`FragmentCategoriesBinding`)
  - RecyclerView with LinearLayoutManager
  - Hilt injection with `@AndroidEntryPoint`
  - Example ViewModel injection
- **Layout:** `fragment_categories.xml` with RecyclerView placeholder

### WallpaperListFragment
**Location:** `ui/wallpaperlist/`
- **Features:**
  - ViewBinding (`FragmentWallpaperListBinding`)
  - RecyclerView with GridLayoutManager (2 columns)
  - Navigation arguments using Safe Args (`WallpaperListFragmentArgs`)
  - Hilt injection with `@AndroidEntryPoint`
- **Layout:** `fragment_wallpaper_list.xml` with RecyclerView placeholder

### PreviewFragment
**Location:** `ui/preview/`
- **Features:**
  - ViewBinding (`FragmentPreviewBinding`)
  - ImageView for wallpaper preview
  - FloatingActionButton for download action
  - Navigation arguments using Safe Args (`PreviewFragmentArgs`)
  - Hilt injection with `@AndroidEntryPoint`
- **Layout:** `fragment_preview.xml` with ImageView and FAB

## Key Features Implemented

### ViewBinding
All activities and fragments use ViewBinding:
- Type-safe view access
- Null-safe binding with proper lifecycle management
- Binding cleared in `onDestroyView()` for fragments

### Proper Lifecycle Usage
- Fragments properly implement lifecycle callbacks
- ViewBinding nullability handled correctly
- Safe reference to binding using `get()` property

### Hilt Integration
- Application class annotated with `@HiltAndroidApp`
- Modules use `@Module` and `@InstallIn(SingletonComponent::class)`
- Activities and Fragments use `@AndroidEntryPoint`
- ViewModels use `@HiltViewModel` with `@Inject` constructor

### Navigation
- Type-safe navigation arguments using Safe Args
- Proper animation transitions between screens
- Action bar integration with up navigation

## Gradle Configuration

All required dependencies are already configured in:
- `app/build.gradle.kts`:
  - Hilt plugin: `com.google.dagger.hilt.android`
  - KSP plugin: `com.google.devtools.ksp`
  - ViewBinding enabled
  - All necessary dependencies (Hilt, Navigation, Lifecycle, etc.)

## Permissions
Added to `AndroidManifest.xml`:
- `INTERNET` - For network requests
- `ACCESS_NETWORK_STATE` - For network state monitoring

## Next Steps

To complete the implementation:
1. Implement actual data fetching in `WallpaperRepositoryImpl`
2. Create RecyclerView adapters for Categories and WallpaperList
3. Add API service interface and integrate with Retrofit
4. Implement ViewModels for all fragments
5. Add image loading library (e.g., Coil or Glide)
6. Implement download functionality in PreviewFragment
7. Add error handling and loading states

## Testing the Build

The project is configured and ready to build. All Gradle plugins are set up:
- Android Application Plugin
- Kotlin Android Plugin
- Kotlin KSP Plugin
- Hilt Android Plugin

To build the project:
```bash
./gradlew build
```

Note: The build requires network access to download dependencies.
