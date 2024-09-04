package com.rksrtx76.cinemax.main.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService.Companion.API_KEY
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.main.domain.repository.GenreRepository
import com.rksrtx76.cinemax.main.domain.repository.MediaRepository
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.Constants.ALL
import com.rksrtx76.cinemax.util.Constants.MOVIE
import com.rksrtx76.cinemax.util.Constants.POPULAR
import com.rksrtx76.cinemax.util.Constants.TOP_RATED
import com.rksrtx76.cinemax.util.Constants.TRENDING_TIME
import com.rksrtx76.cinemax.util.Constants.TV
import com.rksrtx76.cinemax.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val genreRepository: GenreRepository
): ViewModel() {

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    val showSplashScreen = mutableStateOf(false)

    init {
        load()
        viewModelScope.launch {
            delay(500)
            showSplashScreen.value = false
        }
    }

    private fun load(fetchFromRemote : Boolean = false){
        loadPopularMovies(fetchFromRemote)
        loadTopRatedMovies(fetchFromRemote)
        loadNowPlayingMovies(fetchFromRemote)

        loadTopRatedTvSeries(fetchFromRemote)
        loadPopularTvSeries(fetchFromRemote)

        loadTrendingAll(fetchFromRemote)

        loadUpcomingMoviesList(fetchFromRemote)
        loadAiringTvSeriesList(fetchFromRemote)


        loadGenres(fetchFromRemote = fetchFromRemote, isMovies = true)
        loadGenres(fetchFromRemote = fetchFromRemote, isMovies = false)

        loadBookmarkedMedia()

    }

    fun onEvent(event: MainUiEvents){
        when(event){
            is MainUiEvents.Refresh -> {
                _mainUiState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                loadBookmarkedMedia()

                loadGenres(
                    fetchFromRemote = true,
                    isMovies = true
                )
                loadGenres(
                    fetchFromRemote = true,
                    isMovies = false
                )

                when(event.type){
                    Constants.homeScreen ->{
                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadUpcomingMoviesList(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadAiringTvSeriesList(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.popularScreen ->{
                        loadPopularMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.trendingAllListScreen ->{
                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.tvSeriesScreen ->{
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.topRatedAllListScreen ->{
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        // calling it to have equal top rated and tv series items,
                        // because loadTopRatedMovies() and loadPopularTvSeries() are already
                        // used to create the tvSeries list. So if i don't call it here I we will
                        // have more TopRated items than Popular ones.
//                        loadPopularTvSeries(
//                            fetchFromRemote = true,
//                            isRefresh = true
//                        )
                    }
                    Constants.NOW_PLAYING ->{
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }
                    Constants.recommendedListScreen ->{
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadPopularMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                        )
                    }
                    Constants.upcomingMoviesScreen ->{
                        loadUpcomingMoviesList(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }
                }

            }
            is MainUiEvents.OnPaginate -> {
                when(event.type){
                    Constants.trendingAllListScreen ->{
                        loadTrendingAll(true)
                    }

                    Constants.topRatedAllListScreen ->{
                        loadTopRatedMovies(true)
                        loadTopRatedTvSeries(true)
                        // calling it to have equal top rated and tv series items,
                        // because loadTopRatedMovies() and loadPopularTvSeries() are already
                        // used to create the tvSeries list. So if i don't call it here I we will
                        // have more TopRated items than Popular ones.
//                        loadPopularTvSeries(true)
                    }

                    Constants.popularScreen ->{
                        Timber.tag(Constants.GET_TAG).d("OnPaginate : popularScreen")
                        loadPopularMovies(true)
                        loadPopularTvSeries(true)
                    }

                    Constants.tvSeriesScreen ->{
                        loadPopularTvSeries(true)
                        loadTopRatedTvSeries(true)
                    }

                    Constants.recommendedListScreen ->{
                        loadTopRatedTvSeries(true)
                    }
                }
            }
        }
    }

    private fun loadGenres(
        fetchFromRemote: Boolean,
        isMovies: Boolean
    ){
        viewModelScope.launch {
            if(isMovies){
                genreRepository
                    .getGenres(fetchFromRemote,MOVIE, API_KEY)
                    .collect{ result->
                        when(result){
                            is Resource.Error -> Unit
                            is Resource.Loading -> Unit
                            is Resource.Success -> {
                                result.data?.let { genresList->
                                    _mainUiState.update {
                                        it.copy(
                                            moviesGenresList = genresList
                                        )
                                    }
                                }
                            }
                        }
                    }
            }else{
                genreRepository
                    .getGenres(fetchFromRemote,TV, API_KEY)
                    .collect{ result->
                        when(result){
                            is Resource.Error -> Unit
                            is Resource.Loading -> Unit
                            is Resource.Success -> {
                                result.data?.let { genresList->
                                    _mainUiState.update {
                                        it.copy(
                                            tvGenresList = genresList
                                        )
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun loadPopularMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    POPULAR,
                    mainUiState.value.popularMoviesPage,
                    API_KEY
                )
                .collect{ result ->
                    when(result){
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if(isRefresh){
                                    _mainUiState.update {
                                        it.copy(
                                            popularMoviesList = shuffledMediaList.toList(),
                                            popularMoviesPage = 1
                                        )
                                    }
                                }else{
                                    _mainUiState.update {
                                        it.copy(
                                            popularMoviesList = mainUiState.value.popularMoviesList + shuffledMediaList.toList(),
                                            popularMoviesPage = mainUiState.value.popularMoviesPage + 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun loadTopRatedMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {
        viewModelScope.launch {
            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    TOP_RATED,
                    mainUiState.value.topRatedMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedMoviesList = shuffledMediaList.toList(),
                                            topRatedMoviesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedMoviesList = mainUiState.value.topRatedMoviesList + shuffledMediaList.toList(),
                                            topRatedMoviesPage = mainUiState.value.topRatedMoviesPage + 1
                                        )
                                    }
                                }
                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }
                    }
                }
        }
    }


    private fun loadTopRatedTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    TV,
                    TOP_RATED,
                    mainUiState.value.topRatedTvSeriesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedTvSeriesList = shuffledMediaList.toList(),
                                            topRatedTvSeriesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedTvSeriesList = mainUiState.value.topRatedTvSeriesList + shuffledMediaList.toList(),
                                            topRatedTvSeriesPage = mainUiState.value.topRatedTvSeriesPage + 1
                                        )
                                    }
                                }
                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadPopularTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    TV,
                    POPULAR,
                    mainUiState.value.popularTvSeriesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            popularTvSeriesList = shuffledMediaList.toList(),
                                            popularTvSeriesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            popularTvSeriesList = mainUiState.value.popularTvSeriesList + shuffledMediaList.toList(),
                                            popularTvSeriesPage = mainUiState.value.popularTvSeriesPage + 1
                                        )
                                    }
                                }
                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                            }
                        }
                    }
                }
        }
    }

    private fun loadTrendingAll(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getTrendingList(
                    fetchFromRemote,
                    isRefresh,
                    ALL,
                    TRENDING_TIME,
                    mainUiState.value.trendingAllPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            trendingAllList = shuffledMediaList.toList(),
                                            trendingAllPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            trendingAllList = mainUiState.value.trendingAllList + shuffledMediaList.toList(),
                                            trendingAllPage = mainUiState.value.trendingAllPage + 1
                                        )
                                    }
                                }
                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                            }
                        }
                    }
                }
        }
    }

    private fun loadNowPlayingMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getNowPlayingMoviesList(
                    fetchFromRemote,
                    isRefresh,
                    mainUiState.value.nowPlayingMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            nowPlayingMoviesList = shuffledMediaList.toList(),
                                            nowPlayingMoviesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            nowPlayingMoviesList = mainUiState.value.nowPlayingMoviesList + shuffledMediaList.toList(),
                                            nowPlayingMoviesPage = mainUiState.value.nowPlayingMoviesPage + 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun loadUpcomingMoviesList(
        fetchFromRemote: Boolean = true,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {
            mediaRepository
                .getUpcomingMoviesList(
                    fetchFromRemote,
                    isRefresh,
                    mainUiState.value.upcomingMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            upcomingMoviesList = shuffledMediaList.toList(),
                                            upcomingMoviesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            upcomingMoviesList = mainUiState.value.upcomingMoviesList + shuffledMediaList.toList(),
                                            upcomingMoviesPage = mainUiState.value.upcomingMoviesPage + 1
                                        )
                                    }
                                }
//                                createSpecialList(
//                                    mediaList = mediaList,
//                                    isRefresh = isRefresh
//                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadAiringTvSeriesList(
        fetchFromRemote: Boolean = true,
        isRefresh: Boolean = false
    ) {
        viewModelScope.launch {
            mediaRepository
                .getAiringTodayTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    mainUiState.value.airingTvSeriesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            airingTvSeriesList = shuffledMediaList.toList(),
                                            airingTvSeriesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            airingTvSeriesList = mainUiState.value.airingTvSeriesList + shuffledMediaList.toList(),
                                            airingTvSeriesPage = mainUiState.value.airingTvSeriesPage + 1
                                        )
                                    }
                                }
                                createSpecialList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }
                    }
                }
        }
    }

    fun loadBookmarkedMedia() {
        viewModelScope.launch {
            val bookmarkedMedia = mediaRepository.getBookmarkedMedia()
            _mainUiState.update { it.copy(bookmarkedMedia = bookmarkedMedia) }
        }
    }

    // New function to handle bookmark updates and refresh the bookmarked list
    fun updateBookmark(id: Int, isBookmarked: Boolean) {
        viewModelScope.launch {
            mediaRepository.bookmarkMedia(id, isBookmarked)
            loadBookmarkedMedia()
        }
    }

    private fun createSpecialList(
        mediaList: List<Media>,
        isRefresh: Boolean = false
    ){
        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    specialList = emptyList()
                )
            }
        }

        if(mainUiState.value.specialList.size >= 7){
            return
        }

        val shuffledMediaList = mediaList.take(7).toMutableList()
        shuffledMediaList.shuffle()

        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    specialList = shuffledMediaList
                )
            }
        }else{
            _mainUiState.update {
                it.copy(
                    specialList = mainUiState.value.specialList + shuffledMediaList
                )
            }
        }

        for(item in mainUiState.value.specialList){
            Timber.tag("special_list").d(item.title)
        }
    }


    private fun createTvSeriesList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ){
        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    tvSeriesList = shuffledMediaList.toList()
                )
            }
        }else{
            _mainUiState.update {
                it.copy(
                    tvSeriesList = mainUiState.value.tvSeriesList + shuffledMediaList.toList()
                )
            }
        }
    }

    private fun createTopRatedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ){
        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    topRatedAllList = shuffledMediaList.toList()
                )
            }
        }else{
            _mainUiState.update {
                it.copy(
                    topRatedAllList = mainUiState.value.topRatedAllList + shuffledMediaList.toList()
                )
            }
        }
    }

    private fun createRecommendedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ){
        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    recommendedAllList = shuffledMediaList.toList()
                )
            }
        }else{
            _mainUiState.update {
                it.copy(
                    recommendedAllList = mainUiState.value.recommendedAllList + shuffledMediaList.toList()
                )
            }
        }
        createSpecialList(
            mediaList = mediaList,
            isRefresh = isRefresh
        )
    }
}