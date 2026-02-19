# Backdrop Wallpaper App - Project Setup Documentation

## Overview
Modern Android Wallpaper Application built with:
- **Language:** Kotlin
- **UI:** XML layouts (NOT Compose)
- **Architecture:** MVVM + Clean Architecture
- **Minimum SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Latest stable)
- **Build System:** Gradle Kotlin DSL

## Project Structure

### Gradle Configuration

#### 1. Project-level build.gradle.kts
Located at: `/build.gradle.kts`

**Features:**
- Uses buildscript classpath approach for reliable plugin resolution
- Configures Android Gradle Plugin 8.3.0
- Kotlin 1.9.22
- Hilt 2.50 for dependency injection
- KSP 1.9.22-1.0.17 for annotation processing

**Key Dependencies:**
```kotlin
classpath("com.android.tools.build:gradle:8.3.0")
classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
```

#### 2. App-level build.gradle.kts
Located at: `/app/build.gradle.kts`

**Plugins:**
- `com.android.application` - Android app plugin
- `org.jetbrains.kotlin.android` - Kotlin Android support
- `com.google.devtools.ksp` - Kotlin Symbol Processing for annotation processing
- `com.google.dagger.hilt.android` - Hilt dependency injection

**Build Features:**
- **ViewBinding:** Enabled for type-safe view access
- **Min SDK:** 24
- **Target SDK:** 34
- **Compile SDK:** 34
- **Java Version:** 11
- **Kotlin JVM Target:** 11

#### 3. Version Catalog (libs.versions.toml)
Located at: `/gradle/libs.versions.toml`

**Version Management:**
```toml
[versions]
agp = "8.3.0"
kotlin = "1.9.22"
ksp = "1.9.22-1.0.17"
lifecycle = "2.7.0"
navigation = "2.7.6"
hilt = "2.50"
retrofit = "2.9.0"
okhttp = "4.12.0"
coroutines = "1.7.3"
```

## Dependencies Included

### Core Android
- `androidx.core:core-ktx` - Kotlin extensions for Android framework
- `androidx.appcompat:appcompat` - Backward compatibility
- `com.google.android.material:material` - Material Design components
- `androidx.activity:activity` - Activity APIs
- `androidx.constraintlayout:constraintlayout` - Flexible layouts

### Architecture Components (MVVM)
- `androidx.lifecycle:lifecycle-viewmodel-ktx` - ViewModel with Kotlin extensions
- `androidx.lifecycle:lifecycle-livedata-ktx` - LiveData with Kotlin extensions
- `androidx.lifecycle:lifecycle-runtime-ktx` - Lifecycle runtime with Kotlin extensions

### Navigation Component
- `androidx.navigation:navigation-fragment-ktx` - Fragment navigation
- `androidx.navigation:navigation-ui-ktx` - Navigation UI utilities

### Dependency Injection - Hilt
- `com.google.dagger:hilt-android` - Hilt Android library
- `com.google.dagger:hilt-compiler` - Hilt annotation processor (KSP)

### Networking
- `com.squareup.retrofit2:retrofit` - REST client
- `com.squareup.retrofit2:converter-gson` - Gson converter for Retrofit
- `com.squareup.okhttp3:okhttp` - HTTP client
- `com.squareup.okhttp3:logging-interceptor` - HTTP logging
- `com.google.code.gson:gson` - JSON serialization/deserialization

### Coroutines + Flow
- `org.jetbrains.kotlinx:kotlinx-coroutines-core` - Coroutines core
- `org.jetbrains.kotlinx:kotlinx-coroutines-android` - Android coroutines support

### Testing
- `junit:junit` - Unit testing framework
- `androidx.test.ext:junit` - Android JUnit extensions
- `androidx.test.espresso:espresso-core` - UI testing

## Key Features

### 1. ViewBinding
ViewBinding is enabled in the app-level build.gradle.kts:
```kotlin
buildFeatures {
    viewBinding = true
}
```

This provides:
- Type-safe view references
- Null-safe view access
- Better performance than findViewById

### 2. Kotlin Symbol Processing (KSP)
KSP is used instead of KAPT for faster annotation processing:
- 2x faster than KAPT
- Used with Hilt for dependency injection
- Configured via `com.google.devtools.ksp` plugin

### 3. Hilt Dependency Injection
Configured for Clean Architecture:
- Application-level DI container
- Automatic dependency graph generation
- Scoped dependencies for ViewModels, Repositories, etc.

### 4. Navigation Component
Ready for single-activity architecture:
- Type-safe navigation
- Deep linking support
- Fragment transactions handled automatically

### 5. MVVM Architecture Ready
All necessary components included:
- ViewModels with lifecycle awareness
- LiveData for observable data
- Repository pattern support via Hilt

## Building the Project

### Prerequisites
1. Android Studio Hedgehog or later
2. JDK 11 or higher
3. Android SDK 34 or higher
4. Internet connection for dependency resolution

### Build Commands

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run all tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

## Network Requirements

**Important:** This project requires access to the following repositories:
- `maven.google.com` - For Android and Google dependencies
- `repo1.maven.org/maven2` - For Maven Central dependencies
- `plugins.gradle.org` - For Gradle plugins

## Clean Architecture Structure (Recommended)

```
app/
├── data/
│   ├── remote/          # API services (Retrofit)
│   ├── local/           # Local database (Room - can be added)
│   ├── repository/      # Repository implementations
│   └── model/           # Data models
├── domain/
│   ├── model/           # Domain models
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Business logic
├── presentation/
│   ├── ui/
│   │   ├── home/        # Home screen (Fragment + ViewModel)
│   │   ├── detail/      # Detail screen
│   │   └── search/      # Search screen
│   ├── adapter/         # RecyclerView adapters
│   └── common/          # Shared UI components
└── di/                  # Hilt modules
```

## Application Name
The app is named **"Backdrop Wallpaper App"** as specified in `/app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Backdrop Wallpaper App</string>
```

## Next Steps

1. **Create Hilt Application Class**
   ```kotlin
   @HiltAndroidApp
   class WallpaperApplication : Application()
   ```

2. **Set up Navigation Graph**
   - Create `res/navigation/nav_graph.xml`
   - Define app navigation flow

3. **Create Data Layer**
   - Define API endpoints with Retrofit
   - Create repository implementations

4. **Create Domain Layer**
   - Define use cases for business logic
   - Create domain models

5. **Create Presentation Layer**
   - Create ViewModels with Hilt injection
   - Design XML layouts
   - Implement Fragments with ViewBinding

## License
[Your License Here]
