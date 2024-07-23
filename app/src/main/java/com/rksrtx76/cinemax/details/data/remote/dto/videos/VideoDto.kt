package com.rksrtx76.cinemax.details.data.remote.dto.videos

data class VideoDto(
    val key: String? = null,
    val site: String? = null,
    val id: String,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val official: Boolean,
    val published_at: String,
    val size: Int,
    val type: String
)