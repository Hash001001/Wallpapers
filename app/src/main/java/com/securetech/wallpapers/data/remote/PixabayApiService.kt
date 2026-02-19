package com.securetech.wallpapers.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {

    @GET("api/")
    suspend fun searchImages(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("image_type") imageType: String = "photo",
        @Query("orientation") orientation: String = "vertical",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1,
        @Query("safesearch") safeSearch: Boolean = true
    ): PixabaySearchResponse
}
