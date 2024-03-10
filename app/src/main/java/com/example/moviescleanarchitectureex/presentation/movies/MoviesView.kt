package com.example.moviescleanarchitectureex.presentation.movies

import com.example.moviescleanarchitectureex.ui.models.MoviesState

interface MoviesView {
    // Методы, меняющие внешний вид экрана
    fun render(state: MoviesState)
    // Методы «одноразовых событий»
    fun showToast(additionalMessage: String?)
}