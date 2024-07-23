package com.rksrtx76.cinemax.details.data.remote.dto.cast

data class CastMemberDto(
    val cast_id: Int,
    val character: String,
    val name: String,
    val profile_path: String?
)