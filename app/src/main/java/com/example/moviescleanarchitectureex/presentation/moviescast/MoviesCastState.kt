package com.example.moviescleanarchitectureex.presentation.moviescast

import com.example.moviescleanarchitectureex.domen.models.MovieCast

interface MoviesCastState {
    object Loading: MoviesCastState
    data class Content(
        val movie: MovieCast
    ): MoviesCastState

    data class Error(
        val message: String
    ): MoviesCastState

}