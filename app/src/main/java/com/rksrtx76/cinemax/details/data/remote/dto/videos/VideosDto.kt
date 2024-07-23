package com.rksrtx76.cinemax.details.data.remote.dto.videos

data class VideosDto(
    val id : Int,
    val results : List<VideoDto>? = null
)