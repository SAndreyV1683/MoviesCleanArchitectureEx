package com.example.moviescleanarchitectureex.domen.db

import com.example.moviescleanarchitectureex.domen.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun historyMovies(): Flow<List<Movie>>

}