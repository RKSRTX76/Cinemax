package com.rksrtx76.cinemax.main.data.remote.dto

data class MediaDto(
    val backdrop_path: String? = null,
    val first_air_date: String? = null,
    val genre_ids: List<Int>? = null,
    val id: Int? = null,
    var media_type: String? = null,
    var origin_country: List<String>? = null,
    val original_language: String? = null,
    val original_name: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val name: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val release_date: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null,
    var category: String? = null,

    val similarMediaList: List<Int>? = null,
    val adult: Boolean? = null,
)
