package com.rksrtx76.cinemax.details.presentation

import com.rksrtx76.cinemax.main.domain.model.CastMember
import com.rksrtx76.cinemax.main.domain.model.Genre
import com.rksrtx76.cinemax.main.domain.model.Media

data class MediaDetailsScreenState(
    val isLoading: Boolean = false,
    val media: Media? = null,
    val videoId: String ="",
    val readableTime:String = "",

    val similarMediaList: List<Media> = emptyList(),
    val smallSimilarMediaList: List<Media> = emptyList(),

    val videoList: List<String> = emptyList(),
    val moviesGenreList: List<Genre> = emptyList(),
    val tvGenresList: List<Genre> = emptyList(),

    val castList: List<CastMember> = emptyList(),

    )