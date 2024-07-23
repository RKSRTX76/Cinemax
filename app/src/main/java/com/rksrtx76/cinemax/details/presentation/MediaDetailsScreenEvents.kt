package com.rksrtx76.cinemax.details.presentation

import com.rksrtx76.cinemax.main.domain.model.Genre

sealed class MediaDetailsScreenEvents {

    data class SetDataAndLoad(
        val moviesGenresList : List<Genre>,
        val tvGenreList : List<Genre>,
        val id : Int,
        val type : String,
        val category : String
    ): MediaDetailsScreenEvents()

    object Refresh : MediaDetailsScreenEvents()

    object NavigateToWatchVideo : MediaDetailsScreenEvents()

    data class ToggleBookmark(val mediaId: Int, val isBookmarked: Boolean) : MediaDetailsScreenEvents()

}