package com.example.moviescleanarchitectureex.ui.models

import com.example.moviescleanarchitectureex.domen.models.MovieDetails

sealed interface AboutState {
    data class Content(
        val movie: MovieDetails
    ): AboutState

    data class Error(
        val message: String
    ): AboutState

}