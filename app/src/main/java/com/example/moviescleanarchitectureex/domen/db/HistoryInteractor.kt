package com.example.moviescleanarchitectureex.domen.db

import com.example.moviescleanarchitectureex.domen.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {
    fun historyMovies(): Flow<List<Movie>>
}