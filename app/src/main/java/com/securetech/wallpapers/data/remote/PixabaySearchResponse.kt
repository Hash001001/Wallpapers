package com.securetech.wallpapers.data.remote

import com.google.gson.annotations.SerializedName

data class PixabaySearchResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("totalHits") val totalHits: Int,
    @SerializedName("hits") val hits: List<PixabayImage>
)

data class PixabayImage(
    @SerializedName("id") val id: Int,
    @SerializedName("webformatURL") val webformatUrl: String,
    @SerializedName("largeImageURL") val largeImageUrl: String,
    @SerializedName("previewURL") val previewUrl: String,
    @SerializedName("tags") val tags: String
)
