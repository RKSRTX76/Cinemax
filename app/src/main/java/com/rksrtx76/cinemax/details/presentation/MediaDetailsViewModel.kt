package com.rksrtx76.cinemax.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rksrtx76.cinemax.details.domain.repository.DetailsRepository
import com.rksrtx76.cinemax.details.domain.repository.ExtraDetailsRepository
import com.rksrtx76.cinemax.details.domain.usecase.MinutesToReadableTime
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService.Companion.API_KEY
import com.rksrtx76.cinemax.main.domain.repository.MediaRepository
import com.rksrtx76.cinemax.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val mediaRepository : MediaRepository,
    private val detailsRepository : DetailsRepository,
    private val extraDetailsRepository: ExtraDetailsRepository,
): ViewModel() {

    private val _mediaDetailsScreenState = MutableStateFlow(MediaDetailsScreenState())
    val mediaDetailsScreenState = _mediaDetailsScreenState.asStateFlow()



    fun onEvent(event : MediaDetailsScreenEvents){
        when(event){
            is MediaDetailsScreenEvents.NavigateToWatchVideo -> {
                _mediaDetailsScreenState.update {
                    it.copy(
                        videoId = mediaDetailsScreenState.value.videoList.shuffled()[0]
                    )
                }
            }
            is MediaDetailsScreenEvents.Refresh -> {
                _mediaDetailsScreenState.update {
                    it.copy(isLoading = true)
                }
                startLoad(true)
            }
            is MediaDetailsScreenEvents.SetDataAndLoad -> {
                _mediaDetailsScreenState.update {
                    it.copy(
                        moviesGenreList = event.moviesGenresList,
                        tvGenresList = event.tvGenreList
                    )
                }
                startLoad(
                    isRefresh = false,
                    id = event.id,
                    type = event.type,
                    category = event.category
                )
            }
            is MediaDetailsScreenEvents.ToggleBookmark -> {
                toggleBookmark(event.mediaId, event.isBookmarked)
            }
        }
    }
    private fun toggleBookmark(mediaId: Int, isBookmarked: Boolean) {
        viewModelScope.launch {
            val updatedMedia = mediaDetailsScreenState.value.media?.copy(isBookmarked = isBookmarked)
            if (updatedMedia != null) {
                // Update the media item in the repository
                mediaRepository.updateBookmarkedMedia(updatedMedia)
                // Update the state with the new media item
                _mediaDetailsScreenState.value = _mediaDetailsScreenState.value.copy(media = updatedMedia)
            }
        }
    }

    private fun startLoad(
        isRefresh : Boolean,
        id : Int = mediaDetailsScreenState.value.media?.id ?: 0,
        type : String = mediaDetailsScreenState.value.media?.mediaType ?: "",
        category : String = mediaDetailsScreenState.value.media?.category ?: ""
    ){
        loadMediaItem(
            id = id,
            type = type,
            category = category
        ){
            loadDetails(isRefresh = isRefresh)

            loadSimilarMediaLit(isRefresh = isRefresh)

            loadVideosList(isRefresh = isRefresh, type = type)

            loadCastList(isRefresh = isRefresh, type = type)

        }
    }


    private fun loadMediaItem(
        id : Int,
        type : String,
        category: String,
        onFinished: ()-> Unit
    ){
        viewModelScope.launch {
            _mediaDetailsScreenState.update {
                it.copy(media = mediaRepository.getItem(
                    type = type,
                    category = category,
                    id = id
                ))
            }
            onFinished()
        }
    }

    private fun loadDetails(isRefresh: Boolean){
        viewModelScope.launch {
            detailsRepository.getDetails(
                id = mediaDetailsScreenState.value.media?.id ?: 0,
                type = mediaDetailsScreenState.value.media?.mediaType ?: "",
                isRefresh = isRefresh,
                apiKey = API_KEY
            )
                .collect{ result->
                    when(result){
                        is Resource.Error -> {
                            Unit
                        }
                        is Resource.Loading -> {
                            _mediaDetailsScreenState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { media ->
                                _mediaDetailsScreenState.update {
                                    it.copy(
                                        media = mediaDetailsScreenState.value.media?.copy(
                                            runtime = media.runtime,
                                            status = media.status,
                                            tagline = media.tagline
                                        ),
                                        readableTime = if (media.runtime != 0) {
                                            MinutesToReadableTime(media.runtime).invoke()
                                        } else ""
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun loadSimilarMediaLit(isRefresh: Boolean){
        viewModelScope.launch {
            extraDetailsRepository.getSimilarMediaList(
                isRefresh = isRefresh,
                id = mediaDetailsScreenState.value.media?.id ?: 0,
                type = mediaDetailsScreenState.value.media?.mediaType ?: "",
                page = 1,
                apiKey = API_KEY
            )
                .collect{ result ->
                    when(result){
                        is Resource.Error -> {
                            // do nothing
                        }
                        is Resource.Loading -> {
                            _mediaDetailsScreenState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { similarMediaList->
                                _mediaDetailsScreenState.update {
                                    it.copy(
                                        similarMediaList = similarMediaList,
                                        smallSimilarMediaList = similarMediaList.take(10)
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun loadVideosList(isRefresh: Boolean, type: String){
        viewModelScope.launch {
            _mediaDetailsScreenState.update {
                it.copy(videoList = emptyList())  // clear previous list
            }
            extraDetailsRepository.getVideosList(
                id = mediaDetailsScreenState.value.media?.id ?: 0,
                isRefresh = isRefresh,
                apiKey = API_KEY,
                type = type
            )
                .collect{ result ->
                    when(result){
                        is Resource.Error -> {
                            // do nothing
                        }
                        is Resource.Loading -> {
                            _mediaDetailsScreenState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { videoList->
                                _mediaDetailsScreenState.update {
                                    it.copy(
                                        videoList = videoList
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun loadCastList(isRefresh: Boolean, type: String) {
        viewModelScope.launch {
            _mediaDetailsScreenState.update {
                it.copy(
                    castList = emptyList(), // Clear the existing cast list
                    isLoading = true,
                )
            }

            extraDetailsRepository.getCastList(
                isRefresh = isRefresh,
                type = type,
                id = mediaDetailsScreenState.value.media?.id ?: 0,
                apiKey = API_KEY
            ).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _mediaDetailsScreenState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _mediaDetailsScreenState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { castList ->
                            _mediaDetailsScreenState.update {
                                it.copy(
                                    castList = castList,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}