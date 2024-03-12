package com.example.moviescleanarchitectureex.presentation.moviescast

import com.example.moviescleanarchitectureex.domen.models.MovieCast

interface MoviesCastState {
    object Loading: MoviesCastState
    data class Content(
        val fullTitle: String,
        // Поменяли тип ячеек на более общий
        val items: List<RVItem>,
    ) : MoviesCastState

    data class Error(
        val message: String
    ): MoviesCastState

}