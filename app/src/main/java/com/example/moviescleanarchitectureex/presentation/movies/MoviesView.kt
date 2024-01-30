package com.example.moviescleanarchitectureex.presentation.movies

import com.example.moviescleanarchitectureex.domen.models.Movie

interface MoviesView {
    // Методы, меняющие внешний вид экрана

    // Состояние «загрузки»
    fun showLoading()

    // Состояние «ошибки»
    fun showError(errorMessage: String)

    // Состояние «пустого списка»
    fun showEmpty(emptyMessage: String)

    // Состояние «контента»
    fun showContent(movies: List<Movie>)

    // Методы «одноразовых событий»

    fun showToast(additionalMessage: String)
}