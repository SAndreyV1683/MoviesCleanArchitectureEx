package com.example.moviescleanarchitectureex.presentation.moviescast

import com.example.moviescleanarchitectureex.domen.models.MovieCast

interface MoviesCastState {
    object Loading: MoviesCastState
    data class Content(
        val fullTitle: String,
        val items: List<MoviesCastRVItem>
    ): MoviesCastState

    data class Error(
        val message: String
    ): MoviesCastState

}