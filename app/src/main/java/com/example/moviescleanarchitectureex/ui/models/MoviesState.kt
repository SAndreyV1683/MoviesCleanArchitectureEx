package com.example.moviescleanarchitectureex.ui.models

import com.example.moviescleanarchitectureex.domen.models.Movie

sealed interface MoviesState {
    data object Loading : MoviesState
    data class Content(
        val movies: List<Movie>
    ) : MoviesState
    data class Error(
        val message: String
    ): MoviesState
    data class Empty(
        val message: String
    ) : MoviesState
}