package com.rksrtx76.cinemax.main.data.local.media

import androidx.room.Entity
import androidx.room.PrimaryKey

// We can directly use Media as entity since it is a data class
// but it's better to use MediaEntity to maintainability that's why we extra created a mapper file
// to assign default values

@Entity
data class MediaEntity(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdropPath: String,
    val firstAirDate: String,
    val genreIds: String,
    var mediaType: String,
    val originCountry: String,
    val originalLanguage: String,
    val originalName: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    var category: String,
    var runtime: Int,
    var videosIds: String,
    var status: String,
    var tagline: String,
    var similarMediaList: String,

    var isBookmarked: Boolean = false
)
