package com.securetech.wallpapers.di

import com.securetech.wallpapers.data.repository.FakeWallpaperRepository
import com.securetech.wallpapers.domain.repository.WallpaperRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWallpaperRepository(
        fakeWallpaperRepository: FakeWallpaperRepository
    ): WallpaperRepository
}
