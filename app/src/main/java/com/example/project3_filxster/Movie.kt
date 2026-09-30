package com.example.project3_filxster

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val results: List<Movie>?
)

data class Movie(
    @SerializedName("title") val title: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?
) {
    val posterImageUrl: String
        get() = "https://image.tmdb.org/t/p/w500/$posterPath"
}
