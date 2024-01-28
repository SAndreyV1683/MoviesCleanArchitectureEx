package com.example.moviescleanarchitectureex.presentation.movies

import com.example.moviescleanarchitectureex.domen.models.Movie

interface MoviesView {
    fun showPlaceholderMessage(isVisible: Boolean)

    fun showMoviesList(isVisible: Boolean)

    fun showProgressBar(isVisible: Boolean)

    fun changePlaceholderText(newPlaceholderText: String)

    fun updateMoviesList(newMoviesList: List<Movie>)

    fun showMessage(message: String)
}