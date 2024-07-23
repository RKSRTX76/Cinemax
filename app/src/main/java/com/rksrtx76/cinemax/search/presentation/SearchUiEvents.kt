package com.rksrtx76.cinemax.search.presentation

import com.rksrtx76.cinemax.main.domain.model.Media

sealed class SearchUiEvents {
    data class Refresh(val type : String) : SearchUiEvents()
    data class OnPaginate(val type : String) : SearchUiEvents()
    data class OnSearchQueryChanged(val query : String) : SearchUiEvents()
    data class OnSearchedItemClick(val media : Media) : SearchUiEvents()
}