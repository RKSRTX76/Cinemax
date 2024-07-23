package com.rksrtx76.cinemax.main.presentation

import com.rksrtx76.cinemax.main.domain.model.Genre
import com.rksrtx76.cinemax.main.domain.model.Media

data class MainUiState(
    val popularMoviesPage: Int = 1,
    val topRatedMoviesPage: Int = 1,
    val nowPlayingMoviesPage: Int = 1,

    val popularTvSeriesPage: Int = 1,
    val topRatedTvSeriesPage: Int = 1,

    val trendingAllPage: Int = 1,
    val upcomingMoviesPage: Int = 1,
    val airingTvSeriesPage: Int = 1,

    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val areListsToBuildSpecialistEmpty: Boolean = true,

    val popularMoviesList : List<Media> = emptyList(),
    val topRatedMoviesList : List<Media> = emptyList(),
    val nowPlayingMoviesList: List<Media> = emptyList(),
    val upcomingMoviesList : List<Media> = emptyList(),
    val airingTvSeriesList : List<Media> = emptyList(),

    val popularTvSeriesList: List<Media> = emptyList(),
    val topRatedTvSeriesList: List<Media> = emptyList(),

    val trendingAllList: List<Media> = emptyList(),

    val tvSeriesList: List<Media> = emptyList(),
    val moviesList: List<Media> = emptyList(),

    val topRatedAllList: List<Media> = emptyList(),

    val recommendedAllList: List<Media> = emptyList(),

    val specialList: List<Media> = emptyList(),

    val moviesGenresList: List<Genre> = emptyList(),
    val tvGenresList: List<Genre> = emptyList(),

    val bookmarkedMedia: List<Media> = emptyList()

)