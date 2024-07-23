package com.rksrtx76.cinemax.search.presentation

import com.rksrtx76.cinemax.main.domain.model.Media

data class SearchScreenState(
    val searchPage : Int= 1,
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val searchQuery : String = "",
    val searchList : List<Media> = emptyList()
)