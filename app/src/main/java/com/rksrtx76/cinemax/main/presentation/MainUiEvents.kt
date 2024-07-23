package com.rksrtx76.cinemax.main.presentation

sealed class MainUiEvents {
    data class Refresh(val type : String) : MainUiEvents()
    data class OnPaginate(val type : String) : MainUiEvents()
}