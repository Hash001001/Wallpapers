# Backdrop Wallpaper App

A modern Android wallpaper application built with Kotlin, XML layouts, MVVM architecture, and Clean Architecture principles.

## 🎯 Project Overview

This is a professional Android application setup configured with industry-standard tools and libraries for building a wallpaper app.

### Key Features
- ✅ **Kotlin** - Modern programming language for Android
- ✅ **XML Layouts** - Traditional Android UI (Not Jetpack Compose)
- ✅ **MVVM Architecture** - Model-View-ViewModel pattern
- ✅ **Clean Architecture** - Separation of concerns with layers
- ✅ **Dependency Injection** - Hilt for DI
- ✅ **ViewBinding** - Type-safe view access
- ✅ **Coroutines + Flow** - Asynchronous programming
- ✅ **Retrofit + OkHttp** - Network communication
- ✅ **Navigation Component** - Fragment navigation

## 📱 Technical Specifications

| Specification | Value |
|--------------|-------|
| Minimum SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |
| Compile SDK | 34 |
| Language | Kotlin 1.9.22 |
| Build System | Gradle Kotlin DSL |
| AGP Version | 8.3.0 |

## 🏗️ Architecture

The app is structured following Clean Architecture principles:

```
app/
├── data/           # Data layer (API, Database, Repository implementations)
├── domain/         # Domain layer (Business logic, Use cases)
├── presentation/   # UI layer (Fragments, ViewModels, Adapters)
└── di/             # Dependency Injection modules
```

## 📦 Dependencies

### Core
- AndroidX Core KTX
- AppCompat
- Material Design Components
- ConstraintLayout

### Architecture Components
- Lifecycle ViewModel
- LiveData
- Navigation Component

### Dependency Injection
- Hilt 2.50
- KSP (Kotlin Symbol Processing)

### Networking
- Retrofit 2.9.0
- OkHttp 4.12.0
- Gson Converter

### Concurrency
- Kotlin Coroutines 1.7.3
- Flow

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 34

### API Key Setup

This app uses the [Pixabay API](https://pixabay.com/api/docs/) to fetch wallpaper images. To get wallpapers working:

1. Sign up for a free API key at [pixabay.com/api/docs/](https://pixabay.com/api/docs/)
2. Add your API key to `gradle.properties` (project root):
   ```properties
   PIXABAY_API_KEY=your_api_key_here
   ```
3. Rebuild the project

### Building the Project

1. Clone the repository
```bash
git clone https://github.com/Hash001001/Wallpapers.git
cd Wallpapers
```

2. Open in Android Studio
```bash
# Open the project in Android Studio
# File > Open > Select the project directory
```

3. Sync Gradle
```bash
./gradlew clean build
```

4. Run on device/emulator
```bash
./gradlew installDebug
```

## 📚 Documentation

For detailed setup information, dependency versions, and architecture guidelines, see [PROJECT_SETUP.md](PROJECT_SETUP.md).

## 🛠️ Build Configuration Files

The project uses Gradle Kotlin DSL with the following key files:

- **build.gradle.kts** - Project-level build configuration
- **app/build.gradle.kts** - App module configuration
- **gradle/libs.versions.toml** - Centralized version catalog
- **settings.gradle.kts** - Project settings and repositories

## 🎨 Key Features Ready to Implement

The project is set up and ready for implementing:

1. **Wallpaper Browsing** - Grid/List view of wallpapers
2. **Wallpaper Details** - Full-screen preview
3. **Search & Filter** - Find wallpapers by category/tags
4. **Download & Set** - Download and set as wallpaper
5. **Favorites** - Save favorite wallpapers
6. **Categories** - Browse by categories

## 🔧 Development Tools

- **ViewBinding** - Enabled for type-safe view access
- **KSP** - Faster annotation processing than KAPT
- **Hilt** - Compile-time dependency injection
- **Navigation Component** - Type-safe navigation

## 📝 Next Steps

1. Create Hilt Application class
2. Set up Navigation Graph
3. Define API endpoints with Retrofit
4. Implement Repository pattern
5. Create ViewModels with business logic
6. Design XML layouts for screens
7. Implement RecyclerView adapters

## 📄 License

[Your License Here]

## 👥 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

**Note:** This project requires network access to maven.google.com for downloading Android dependencies during build.
