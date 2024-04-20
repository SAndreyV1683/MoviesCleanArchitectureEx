package com.example.moviescleanarchitectureex.presentation.history

import com.example.moviescleanarchitectureex.domen.models.Movie

sealed interface HistoryState {

    data object Loading : HistoryState

    data class Content(
        val movies: List<Movie>
    ) : HistoryState

    data class Empty(
        val message: String
    ) : HistoryState

}