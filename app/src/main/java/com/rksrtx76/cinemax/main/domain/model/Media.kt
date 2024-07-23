package com.rksrtx76.cinemax.main.domain.model

data class Media(
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    val id: Int,
    val mediaType: String,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int,
    var category: String,
    var runtime: Int,
    var status: String?,
    var tagline: String?,
    var videosIds: List<String>,
    var similarMediaList: List<Int>,

    var isBookmarked: Boolean
)