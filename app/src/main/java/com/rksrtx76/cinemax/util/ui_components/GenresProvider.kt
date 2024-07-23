package com.rksrtx76.cinemax.util.ui_components

import androidx.compose.runtime.Composable
import com.rksrtx76.cinemax.main.domain.model.Genre

@Composable
fun GenresProvider(
    genre_ids : List<Int>,
    allGenres : List<Genre>
): String {

    var genres = ""

    for (i in allGenres.indices){
        for(j in genre_ids.indices){
            if(allGenres[i].id == genre_ids[j]){
                genres += "${allGenres[i].name} - "
            }
        }
    }

    return genres.dropLastWhile {
        it == ' ' || it == '-'
    }
}