package com.example.moviescleanarchitectureex.presentation.moviescast

import com.example.moviescleanarchitectureex.domen.models.MovieCastPerson

sealed interface MoviesCastRVItem: RVItem {
    data class HeaderItem(
        val headerText: String
    ): MoviesCastRVItem

    data class PersonItem(
        val data: MovieCastPerson
    ): MoviesCastRVItem
}