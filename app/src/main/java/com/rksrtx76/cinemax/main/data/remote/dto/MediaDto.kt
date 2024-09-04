package com.rksrtx76.cinemax.main.data.remote.dto

//in a DTO (Data Transfer Object) file, it's common practice to name the variables exactly as they appear in the API response
// it is also used for separation to app
// If the API changes its response format, you only need to update the DTO and conversion logic,
// rather than affecting your entire app. This helps in maintaining a clear boundary between your app's logic and the external API.

// I can use Media also
// You'd need to annotate every property in Media with @SerializedName
// (or a similar annotation, depending on the library) to match the API's field names.
// This can clutter your domain model and tightly couple it with the API's format.

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
